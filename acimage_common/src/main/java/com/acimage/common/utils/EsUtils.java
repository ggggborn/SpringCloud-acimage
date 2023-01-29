package com.acimage.common.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsDeleteDto;
import com.acimage.common.model.mq.dto.EsUpdateDto;
import com.acimage.common.model.page.Page;
import com.acimage.common.utils.common.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@ConditionalOnClass(ElasticsearchRestTemplate.class)
public class EsUtils {
    @Autowired
    ElasticsearchRestTemplate esTemplate;

    public void createIndexIfNotExist(Class<?> entityType) {
        IndexCoordinates indexCoordinates = indexCoordinatesOf(entityType);
        IndexOperations indexOperations = esTemplate.indexOps(indexCoordinates);
        if (!indexOperations.exists()) {
            // 创建索引和映射
            indexOperations.create();
            indexOperations.refresh();
            Document mapping = indexOperations.createMapping(entityType);
            indexOperations.refresh();
            indexOperations.putMapping(mapping);
            indexOperations.refresh();
            log.info("创建索引和映射关系成功 {}", entityType.getSimpleName());
        }
    }


    public void update(EsUpdateDto updateDto) {
        Object entity = updateDto.object();
        if (entity == null) {
            return;
        }
        List<String> columns = updateDto.getColumns();
        if (CollectionUtil.isEmpty(columns)) {
            return;
        }
        //获取id
        Class<?> indexClass = entity.getClass();
        String id = ReflectUtils.getAnnotatedFiled(entity, Id.class).toString();

        IndexCoordinates indexCoordinates = indexCoordinatesOf(indexClass);
        Document document = Document.create();
        //根据要更新的字段创建对应map
        for (String column : columns) {
            Object value = BeanUtil.getFieldValue(entity, column);
            if (value instanceof Date) {
                String formatDate = DateUtil.format((Date) value, DatePattern.NORM_DATETIME_PATTERN);
                document.put(column, formatDate);
            } else {
                document.put(column, value);
            }
        }

        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .build();
        esTemplate.update(updateQuery, indexCoordinates);

    }

    public void save(EsAddDto esAddDto) {
        Object obj = esAddDto.object();
        esTemplate.save(obj);
    }

    public void remove(EsDeleteDto esDeleteDto) {
        esTemplate.delete(esDeleteDto.getId(), esDeleteDto.getType());
    }

    public IndexCoordinates indexCoordinatesOf(Class<?> clz) {
        String indexName = clz.getAnnotation(org.springframework.data.elasticsearch.annotations.Document.class)
                .indexName();
        return IndexCoordinates.of(indexName);
    }

    public <T> Page<T> termQuery(String column, Object value, Class<T> clz, int pageNo, int pageSize) {
        QueryBuilder queryBuilder = QueryBuilders.termQuery(column, value);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        SearchHits<T> search = esTemplate.search(nativeSearchQuery, clz);
        int totalCount = (int) search.getTotalHits();
        List<T> dateList = search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new Page<>(totalCount,dateList);
    }

}

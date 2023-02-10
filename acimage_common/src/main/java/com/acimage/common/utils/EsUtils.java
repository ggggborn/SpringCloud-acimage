package com.acimage.common.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsDeleteDto;
import com.acimage.common.model.mq.dto.EsUpdateByIdDto;
import com.acimage.common.model.mq.dto.EsUpdateByTermDto;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.utils.common.BeanUtils;
import com.acimage.common.utils.common.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;
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
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    public void updateById(EsUpdateByIdDto updateDto) {
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
        String id = Objects.requireNonNull(ReflectUtils.getAnnotatedFiled(entity, Id.class)).toString();

        IndexCoordinates indexCoordinates = indexCoordinatesOf(indexClass);
        Document document = this.createDocument(entity, columns);

        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .build();
        esTemplate.update(updateQuery, indexCoordinates);
    }

    public void UpdateByTerm(EsUpdateByTermDto updateDto) {
        Object entity = updateDto.object();
        if (entity == null) {
            return;
        }
        List<String> columns = updateDto.getColumns();
        if (CollectionUtil.isEmpty(columns)) {
            return;
        }
        //获取更新依据的term
        Class<?> indexClass = entity.getClass();
        String termColumn = updateDto.getTermColumn();
        Object termValue = BeanUtil.getFieldValue(entity, termColumn);

        IndexCoordinates indexCoordinates = indexCoordinatesOf(indexClass);
        Document document = this.createDocument(entity, columns);

        QueryBuilder queryBuilder = QueryBuilders.termQuery(termColumn, termValue);
        Query query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder).build();
        UpdateQuery updateQuery = UpdateQuery.builder(query)
                .withDocument(document)
                .build();

        esTemplate.updateByQuery(updateQuery, indexCoordinates);
    }

    public <T> void batchUpdateById(List<T> entityList, List<String> columns) {
        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }
        Class<?> clz=entityList.get(0).getClass();
        List<UpdateQuery> updateQueries = new ArrayList<>();

        for (T entity : entityList) {
            String id = Objects.requireNonNull(ReflectUtils.getAnnotatedFiled(entity, Id.class)).toString();
            Document document = this.createDocument(entity, columns);

            UpdateQuery updateQuery = UpdateQuery.builder(id)
                    .withDocument(document)
                    .build();
            updateQueries.add(updateQuery);
        }

        esTemplate.bulkUpdate(updateQueries, clz);
    }

    public void save(EsAddDto esAddDto) {
        Object obj = esAddDto.object();
        esTemplate.save(obj);
    }

    public void remove(EsDeleteDto esDeleteDto) {
        esTemplate.delete(esDeleteDto.getId(), esDeleteDto.getType());
    }


    public <T> MyPage<T> termQuery(String column, Object value, Class<T> clz, int pageNo, int pageSize) {
        QueryBuilder queryBuilder = QueryBuilders.termQuery(column, value);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        SearchHits<T> search = esTemplate.search(nativeSearchQuery, clz);
        int totalCount = (int) search.getTotalHits();
        List<T> dateList = toList(search.getSearchHits());
        return new MyPage<>(totalCount, dateList);
    }

    public <T> List<T> similarQuery(String id, Class<T> index, List<String> fields, int pageNo, int pageSize) {
        MoreLikeThisQuery moreLikeThisQuery = new MoreLikeThisQuery();
        moreLikeThisQuery.setId(id);
        moreLikeThisQuery.addFields(fields.toArray(fields.toArray(new String[0])));
        moreLikeThisQuery.setPageable(PageRequest.of(pageNo - 1, pageSize));
        moreLikeThisQuery.setMinTermFreq(1);
        moreLikeThisQuery.setMinDocFreq(2);
        log.debug("{}", esTemplate.search(moreLikeThisQuery, index).getSearchHits());
        return toList(esTemplate.search(moreLikeThisQuery, index).getSearchHits());
    }

    public <T> List<T> matchQuery(Class<T> index, String field, Object value, int pageNo, int pageSize, float score) {
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery(field, value);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery)
                .withPageable(pageable)
                .withMinScore(score)
                .build();

        SearchHits<T> search = esTemplate.search(nativeSearchQuery, index);
        log.debug("{}", search.getSearchHits());
        return toList(search.getSearchHits());
    }


    public IndexCoordinates indexCoordinatesOf(Class<?> clz) {
        String indexName = clz.getAnnotation(org.springframework.data.elasticsearch.annotations.Document.class)
                .indexName();
        return IndexCoordinates.of(indexName);

    }

    private <T> List<T> toList(List<SearchHit<T>> searchHits) {
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private Document createDocument(Object entity, List<String> columns) {
        Document document = Document.create();
        //根据要更新的字段创建对应map
        for (String column : columns) {
            Object value = BeanUtil.getFieldValue(entity, column);
//            if (value instanceof Date) {
//                String formatDate = DateUtil.format((Date) value, DatePattern.NORM_DATETIME_PATTERN);
//                document.put(column, formatDate);
//            } else {
            document.put(column, value);
//            }
        }
        return document;
    }

}

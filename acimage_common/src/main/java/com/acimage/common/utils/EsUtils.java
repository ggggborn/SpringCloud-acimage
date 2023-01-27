package com.acimage.common.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsUpdateDto;
import com.acimage.common.utils.common.ReflectUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.util.List;

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
            Document mapping=indexOperations.createMapping(entityType);
            indexOperations.putMapping(mapping);
            log.info("创建索引和映射关系成功 {}", entityType.getSimpleName());
        }
    }

    public void update(Class<?> indexClass, String id) {
        IndexCoordinates indexCoordinates = indexCoordinatesOf(indexClass);
        Document document = Document.create();
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .build();
        esTemplate.update(updateQuery, indexCoordinates);
    }

    public void update(EsUpdateDto updateDto) {
        Object entity = updateDto.getObject();
        if (entity == null) {
            return;
        }
        List<SFunction<Object, Object>> columns = updateDto.getColumns();
        if (CollectionUtil.isEmpty(columns)) {
            return;
        }
        //获取id
        Class<?> indexClass = entity.getClass();
        String id = ReflectUtils.getAnnotatedFiled(entity, Id.class).toString();

        IndexCoordinates indexCoordinates = indexCoordinatesOf(indexClass);
        Document document = Document.create();
        //根据要更新的字段创建对应map
        for (SFunction<Object, Object> column : columns) {
            String key = LambdaUtils.columnNameOf(column);
            Object value = column.apply(entity);
            document.put(key, value);
        }

        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .build();
        esTemplate.update(updateQuery, indexCoordinates);

    }

    public void save(EsAddDto esAddDto) {

        Object obj= esAddDto.getObject();
        esTemplate.save(obj);
    }

    public void remove(String id, Class<?> entityType) {
        esTemplate.delete(id, entityType);
    }

    public IndexCoordinates indexCoordinatesOf(Class<?> clz) {
        String indexName = clz.getAnnotation(org.springframework.data.elasticsearch.annotations.Document.class)
                .indexName();
        return IndexCoordinates.of(indexName);
    }

}

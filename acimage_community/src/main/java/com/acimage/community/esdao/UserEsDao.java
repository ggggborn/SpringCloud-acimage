package com.acimage.community.esdao;

import com.acimage.common.model.Index.TopicIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEsDao extends ElasticsearchRepository<TopicIndex,Long> {

}
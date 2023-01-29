package com.acimage.community.service.tag.impl;

import com.acimage.community.dao.TagTopicDao;
import com.acimage.community.service.tag.TagTopicQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagTopicQueryServiceImpl implements TagTopicQueryService {
    @Autowired
    TagTopicDao tagTopicDao;

    @Override
    public List<Integer> listTagIds(long topicId){
        return tagTopicDao.selectTagIds(topicId);
    }
}

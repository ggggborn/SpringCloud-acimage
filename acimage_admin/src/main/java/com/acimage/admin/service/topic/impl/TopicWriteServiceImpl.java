package com.acimage.admin.service.topic.impl;

import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.service.topic.TopicWriteService;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.result.Result;
import com.acimage.feign.client.TopicClient;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DS(ModuleConstants.COMMUNITY)
public class TopicWriteServiceImpl implements TopicWriteService {

    @Autowired
    TopicClient topicClient;

    @Override
    public void remove(long topicId){
        Result<?> result=topicClient.delete(topicId);
        if(!result.isOk()){
            log.error("话题删除失败 id:{}",topicId);
            throw new BusinessException("话题删除失败");
        }
    }
}

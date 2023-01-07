package com.acimage.community.service.topic;


import com.acimage.community.model.request.TopicAddReq;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;

public interface TopicInfoWriteService {

    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndImages(TopicAddReq topicAddReq);

    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndImagesBak(TopicAddReq topicAddReq);


    @Transactional(rollbackFor = {Exception.class})
    void removeTopicAndImages(long topicId);

}

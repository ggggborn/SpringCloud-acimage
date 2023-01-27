package com.acimage.community.service.topic;


import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicAddReqBak2;
import com.acimage.community.model.request.TopicModifyContentReq;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

public interface TopicInfoWriteService {

    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndImages(TopicAddReqBak2 topicAddReqBak2);

    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndCoverImage(TopicAddReq topicAddReq,MultipartFile coverImage);

    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndImagesBak(TopicAddReqBak2 topicAddReqBak2);


    @Transactional(rollbackFor = {Exception.class})
    void removeTopicAndImages(long topicId);
    @Transactional
    void updateContent(TopicModifyContentReq modifyReq);
}

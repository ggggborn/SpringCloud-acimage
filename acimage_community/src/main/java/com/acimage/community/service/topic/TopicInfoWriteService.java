package com.acimage.community.service.topic;


import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicAddReqBak2;
import com.acimage.community.model.request.TopicModifyHtmlReq;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

public interface TopicInfoWriteService {

    @Deprecated
    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndImages(TopicAddReqBak2 topicAddReqBak2);
    @Deprecated
    @Transactional(rollbackFor = {IOException.class})
    long saveTopicAndImagesBak(TopicAddReqBak2 topicAddReqBak2);


    @Transactional(rollbackFor = {Exception.class})
    long saveTopicInfo(TopicAddReq topicAddReq, MultipartFile coverImage);

    @Transactional(rollbackFor = {Exception.class})
    void removeTopicInfo(long topicId);

    @Transactional
    void updateHtml(TopicModifyHtmlReq modifyReq);
}

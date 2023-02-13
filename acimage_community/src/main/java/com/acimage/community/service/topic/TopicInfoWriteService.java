package com.acimage.community.service.topic;


import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicModifyHtmlReq;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

public interface TopicInfoWriteService {

    @Transactional(rollbackFor = {Exception.class})
    long saveTopicInfo(TopicAddReq topicAddReq, MultipartFile coverImage);

    @Transactional(rollbackFor = {Exception.class})
    void removeTopicInfo(long topicId);

    @Transactional(rollbackFor = {Exception.class})
    void removeTopicInfoWithoutVerification(long topicId);

    @Transactional
    void updateHtml(TopicModifyHtmlReq modifyReq);
}

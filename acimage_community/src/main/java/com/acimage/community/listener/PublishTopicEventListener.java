package com.acimage.community.listener;


import com.acimage.community.listener.event.PublishTopicEvent;
import com.acimage.community.service.userstatistic.UserCsWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PublishTopicEventListener implements ApplicationListener<PublishTopicEvent> {
    @Autowired
    UserCsWriteService userCsWriteService;

    @Override
    public void onApplicationEvent(PublishTopicEvent event) {
        //增加用户发表话题数
        userCsWriteService.updateTopicCountByIncrement(event.getUserId(),1);
    }
}

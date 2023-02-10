package com.acimage.community.listener;


import com.acimage.community.listener.event.TopicEvent;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PublishTopicEventListener implements ApplicationListener<TopicEvent> {
    @Autowired
    CmtyUserWriteService cmtyUserWriteService;

    @Override
    public void onApplicationEvent(TopicEvent event) {
        //增加用户发表话题数
        cmtyUserWriteService.updateTopicCountByIncrement(event.getUserId(),1);
    }
}

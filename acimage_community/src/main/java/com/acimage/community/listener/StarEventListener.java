package com.acimage.community.listener;


import com.acimage.community.listener.event.StarEvent;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StarEventListener implements ApplicationListener<StarEvent> {
    @Autowired
    CmtyUserWriteService cmtyUserWriteService;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    @Override
    public void onApplicationEvent(StarEvent event) {
    }
}

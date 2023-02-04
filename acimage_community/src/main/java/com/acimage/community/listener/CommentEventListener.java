package com.acimage.community.listener;


import com.acimage.community.listener.event.CommentEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener implements ApplicationListener<CommentEvent> {

    @Override
    public void onApplicationEvent(CommentEvent event) {
        //增加用户发表话题数

    }
}

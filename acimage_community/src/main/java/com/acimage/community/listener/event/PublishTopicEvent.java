package com.acimage.community.listener.event;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PublishTopicEvent extends ApplicationEvent {
    private Long userId;
    private Long topicId;

    public PublishTopicEvent(Object source, Long userId, Long topicId) {
        super(source);
        this.userId = userId;
        this.topicId = topicId;
    }
}

package com.acimage.community.listener.event;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CommentEvent extends ApplicationEvent {
    private Long userId;
    private Long topicId;

    public CommentEvent(Object source, Long userId, Long topicId) {
        super(source);
        this.userId = userId;
        this.topicId = topicId;
    }
}

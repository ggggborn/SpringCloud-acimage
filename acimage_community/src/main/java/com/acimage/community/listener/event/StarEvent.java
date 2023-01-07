package com.acimage.community.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class StarEvent extends ApplicationEvent {
    Long topicId;
    Long ownerId;
    Long fromUserId;

    public StarEvent(Object source,  Long topicId, Long ownerId, Long fromUserId) {
        super(source);
        this.topicId = topicId;
        this.ownerId = ownerId;
        this.fromUserId = fromUserId;
    }
}

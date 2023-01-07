package com.acimage.community.service.topic;

import com.acimage.common.model.domain.Topic;
import com.acimage.community.service.topic.enums.TopicAttribute;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public interface TopicSpAttrWriteService {
    void removeAttributes(long topicId);
    void updateStarCountByIncrement(List<Long> topicIds,List<Integer> increments);
    void updateCommentCountByIncrement(List<Long> topicIds,List<Integer> increments);
    void updatePageViewByIncrement(List<Long> topicIds,List<Integer> increments);
    void updateActivityTime(List<Long> topicIds, List<Date> activityTimes);
    void changeActivityTime(long topicId, Date date);
    void increaseStarCount(long topicId, int increment);
    void increaseCommentCount(long topicId, int increment);

}

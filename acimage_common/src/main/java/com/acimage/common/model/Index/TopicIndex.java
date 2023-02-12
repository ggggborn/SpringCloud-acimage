package com.acimage.common.model.Index;


import cn.hutool.core.date.DatePattern;
import com.acimage.common.global.consts.EsConstants;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.domain.community.Tag;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.utils.common.BeanUtils;
import com.acimage.common.utils.common.ListUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(indexName = "topic")
@Setting(replicas = 0)
public class TopicIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String all;

    @Field(type = FieldType.Keyword, store = true)
    private Long userId;
    @Field(type = FieldType.Text, analyzer = EsConstants.IK_MAX_WORD, store = true)
    private String username;
    @Field(type = FieldType.Keyword, index = false, store = true)
    private String photoUrl;

    @Field(type = FieldType.Text, analyzer = EsConstants.IK_MAX_WORD, store = true, copyTo = "all")
    private String content;

    @Field(type = FieldType.Text, analyzer = EsConstants.IK_MAX_WORD, store = true, copyTo = "all")
    private String title;

    @Field(type = FieldType.Integer, store = true)
    private Integer starCount;
    @Field(type = FieldType.Integer, store = true)
    private Integer pageView;
    @Field(type = FieldType.Integer, store = true)
    private Integer commentCount;

    @Field(store = true, index = false, type = FieldType.Keyword)
    private String coverImageUrl;

    @Field(type = FieldType.Date, pattern = DatePattern.NORM_DATETIME_PATTERN, store = true)
    private Date createTime;
    @Field(type = FieldType.Date, pattern = DatePattern.NORM_DATETIME_PATTERN, store = true)
    private Date updateTime;
    @Field(type = FieldType.Date, pattern = DatePattern.NORM_DATETIME_PATTERN, store = true)
    private Date activityTime;

    @Field(type = FieldType.Keyword, store = true)
    List<Integer> tagIds;
    @Field(type = FieldType.Keyword, store = true)
    Integer categoryId;

    public static TopicIndex from(Topic topic) {
        TopicIndex topicIndex = BeanUtils.copyPropertiesTo(topic, TopicIndex.class);
        CmtyUser user = topic.getUser();
        if (user != null) {
            topicIndex.setUsername(user.getUsername());
            topicIndex.setPhotoUrl(user.getPhotoUrl());
        }

        return topicIndex;
    }

    public static Topic toTopic(TopicIndex topicIndex) {
        Topic topic = BeanUtils.copyPropertiesTo(topicIndex, Topic.class);
        CmtyUser user = new CmtyUser();
        user.setUsername(topicIndex.getUsername());
        user.setPhotoUrl(topicIndex.getPhotoUrl());
        topic.setUser(user);
        return topic;
    }

    public static List<Topic> toTopicList(List<TopicIndex> topicIndexList) {
        return topicIndexList.stream()
                .map(TopicIndex::toTopic)
                .collect(Collectors.toList());
    }

    public static MyPage<Topic> toTopicPage(MyPage<TopicIndex> topicPage) {
        List<Topic> topicList = toTopicList(topicPage.getDataList());
        return new MyPage<>(topicPage.getTotalCount(),topicList);
    }


}

package com.acimage.common.model.Index;


import cn.hutool.core.date.DatePattern;
import com.acimage.common.global.consts.EsConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@NoArgsConstructor
@Data
@Document(indexName = "topic", replicas = 0, shards = 1)
public class TopicIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String all;

    @Field(type = FieldType.Keyword, store = true)
    private Long userId;

    @Field(type = FieldType.Text, analyzer = EsConstants.IK_MAX_WORD, store = true, copyTo = "all")
    private String content;

    @Field(type = FieldType.Text, analyzer = EsConstants.IK_MAX_WORD, store = true, copyTo = "all")
    private String title;

    private Integer starCount;
    private Integer pageView;
    private Integer commentCount;

    @Field(store = true, index = false, type = FieldType.Keyword)
    private String coverImageUrl;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = DatePattern.NORM_DATE_PATTERN, store = true)
    private Date createTime;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = DatePattern.NORM_DATE_PATTERN, store = true)
    private Date updateTime;


}

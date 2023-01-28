package com.acimage.common.model.domain.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagTopic {

    @TableId(type = IdType.AUTO)
    Long id;
    Long topicId;
    Integer tagId;
    private Date createTime;
    @TableLogic(delval = "1")
    boolean deleted;
}

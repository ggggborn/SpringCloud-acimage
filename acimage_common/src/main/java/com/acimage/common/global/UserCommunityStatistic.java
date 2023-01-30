package com.acimage.common.global;

import com.acimage.common.model.domain.community.CmtyUser;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class UserCommunityStatistic {
    @TableId
    Long userId;
    Integer topicCount;
    Integer starCount;
    @TableField(exist = false)
    CmtyUser cmtyUser;
}

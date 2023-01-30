package com.acimage.common.model.domain.community;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmtyUser {
    @TableId
    Long id;
    String username;
    String photoUrl;
    Integer topicCount;
    Integer starCount;
}

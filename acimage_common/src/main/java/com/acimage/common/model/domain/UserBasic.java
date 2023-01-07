package com.acimage.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasic {
    @TableId
    Long id;
    String username;
    String photoUrl;
}

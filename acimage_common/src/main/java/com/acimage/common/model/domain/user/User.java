package com.acimage.common.model.domain.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type= IdType.INPUT)
    private Long id;
    private String username;
    private String photoUrl;

    @TableField(exist = false)
    private Integer starCount;
    @TableField(exist = false)
    private Integer topicCount;
    @TableField(exist = false)
    List<Integer> roleIds;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}

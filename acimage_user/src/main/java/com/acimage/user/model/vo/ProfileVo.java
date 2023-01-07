package com.acimage.user.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ProfileVo {
    String username;
    String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date registerTime;
    Integer topicCount;
    Integer starCount;
}

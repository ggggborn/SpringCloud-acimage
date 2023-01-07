package com.acimage.common.model.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdWithPhotoUrl {
    private Long userId;
    private String photoUrl;
}

package com.acimage.common.model.domain.image;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageHash {
    @TableId(type= IdType.INPUT)
    Long imageId;
    Long hashValue;
    Integer hashSum;
    Date createTime;
    @TableField(exist = false)
    Integer distance;

    public ImageHash(Long imageId, Long hashValue, Integer hashSum) {
        this.imageId = imageId;
        this.hashValue = hashValue;
        this.hashSum = hashSum;
    }
}

package com.acimage.common.model.domain.sys;

import com.acimage.common.global.enums.MatchRule;
import com.acimage.common.global.enums.MyHttpMethod;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api {
    public static final int PATH_MIN = 2;
    public static final int PATH_MAX = 200;
    public static final int NOTE_MAX = 100;
    public static final String PATH_PATTERN="(/([a-zA-Z0-9]+|(\\*){1,2}))+";

    @TableId(type = IdType.AUTO)
    Integer id;
    String path;

    MyHttpMethod method;

    Integer permissionId;
    String note;
    boolean enable;
    Date createTime;
    Date updateTime;
    @TableLogic(delval = "1")
    boolean deleted;
    @TableField(exist = false)
    Permission permission;

}

package com.acimage.admin.model.request;

import com.acimage.common.global.enums.MatchRule;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.common.model.domain.sys.Permission;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiAddReq {
    public static final int PATH_MAX = 200;
    public static final int PATH_MIN = 2;
    public static final int NOTE_MAX = 100;


    @NotBlank
    @Size(min= Api.PATH_MIN,max=Api.PATH_MAX)
    String path;
    @NotNull
    MatchRule matchRule;
    @NotNull
    HttpMethod method;
    @Positive
    @NotNull
    Integer permissionId;
    @Size(max=Api.NOTE_MAX)
    String note;
    boolean enable;
}

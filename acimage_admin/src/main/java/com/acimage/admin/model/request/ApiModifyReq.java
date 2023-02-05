package com.acimage.admin.model.request;

import com.acimage.common.global.enums.MatchRule;
import com.acimage.common.global.enums.MyHttpMethod;
import com.acimage.common.model.domain.sys.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiModifyReq {
    public static final int PATH_MAX = 200;
    public static final int PATH_MIN = 2;
    public static final int NOTE_MAX = 100;
    @Positive
    Integer id;
    @Size(min = Api.PATH_MIN, max = Api.PATH_MAX)
    @Pattern(regexp = Api.PATH_PATTERN)
    String path;
    @NotNull
    MatchRule matchRule;
    @NotNull
    MyHttpMethod method;
    @Positive
    @NotNull
    Integer permissionId;
    @Size(max = Api.NOTE_MAX)
    String note;
    boolean enable;
}

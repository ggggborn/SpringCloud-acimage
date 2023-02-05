package com.acimage.admin.model.request;

import com.acimage.common.model.domain.sys.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PermissionAddReq {

    Integer parentId;
    @Size(max = Permission.CODE_MAX,message =Permission.CODE_VALIDATION_MSG)
    String code;
    @Size(max = Permission.NOTE_MAX,message =Permission.NOTE_VALIDATION_MSG)
    String note;
    @NotNull
    @Size(max = Permission.LABEL_MAX,message =Permission.LABEL_VALIDATION_MSG)
    String label;
    @NotNull
    Boolean module;
}

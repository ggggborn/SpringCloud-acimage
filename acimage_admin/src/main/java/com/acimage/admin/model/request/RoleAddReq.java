package com.acimage.admin.model.request;


import com.acimage.common.model.domain.sys.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RoleAddReq {
    @Size(max= Role.ROLE_NAME_MAX,min=Role.ROLE_NAME_MIN,message = Role.ROLE_NAME_VALIDATION_MSG)
    String roleName;
    @Size(max=Role.NOTE_MAX,message = Role.NOTE_VALIDATION_MSG)
    String note;
}

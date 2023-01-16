package com.acimage.admin.model.request;

import com.acimage.common.model.domain.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
public class RoleModifyReq {


    @Positive
    Integer id;
    @Size(max= Role.ROLE_NAME_MAX,min=Role.ROLE_NAME_MIN,message = Role.ROLE_NAME_VALIDATION_MSG)
    String roleName;
    @Size(max=Role.NOTE_MAX,message = Role.NOTE_VALIDATION_MSG)
    String note;
}
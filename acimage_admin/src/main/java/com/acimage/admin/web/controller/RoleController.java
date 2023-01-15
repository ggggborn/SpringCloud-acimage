package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.RoleAddReq;
import com.acimage.admin.model.request.RoleModifyReq;
import com.acimage.admin.service.role.RoleQueryService;
import com.acimage.admin.service.role.RoleWriteService;
import com.acimage.common.model.domain.Role;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/roles")
@Validated
public class RoleController {
    @Autowired
    RoleQueryService roleQueryService;
    @Autowired
    RoleWriteService roleWriteService;

    @GetMapping("/all")
    public Result<List<Role>> queryAll(){
        return Result.ok(roleQueryService.listAll());
    }

    @PostMapping()
    public Result<?> add(@Validated @RequestBody RoleAddReq roleAddReq){
        roleWriteService.save(roleAddReq);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable @Positive Integer id){
        roleWriteService.remove(id);
        return Result.ok();
    }

    @PutMapping()
    public Result<?> modify(@Validated @RequestBody RoleModifyReq roleModifyReq){
        roleWriteService.update(roleModifyReq);
        return Result.ok();
    }
}

package com.acimage.admin.dao.sys;

import com.acimage.common.model.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Nullable;
import java.util.List;

public interface PermissionDao extends BaseMapper<Permission> {

    List<Permission> selectTreeByParentId(@Nullable @Param("parentId") Integer parentId);

    List<Permission> selectPermissionsWithParent(@Param("startIndex") int startIndex,@Param("recordNumber") int recordNumber);


}

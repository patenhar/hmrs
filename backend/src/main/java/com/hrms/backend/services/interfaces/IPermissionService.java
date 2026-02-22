package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.PermissionReqDto;
import com.hrms.backend.dtos.response.PermissionResDto;
import com.hrms.backend.entities.Permission;
import com.hrms.backend.utils.ApiResponse;
import java.util.List;
import java.util.UUID;

public interface IPermissionService {
    List<PermissionResDto> getAllPermissions();

    PermissionResDto getPermissionById(UUID id);

    PermissionResDto addPermission(PermissionReqDto permissionReqDto);

    PermissionResDto updatePermission(UUID id, PermissionReqDto permissionReqDto);

    boolean deletePermission(UUID id);
}

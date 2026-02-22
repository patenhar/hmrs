package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.DepartmentReqDto;
import com.hrms.backend.dtos.response.DepartmentResDto;
import com.hrms.backend.entities.Department;
import com.hrms.backend.services.DepartmentService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<DepartmentResDto>>> getAllDepartments() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All departments fetched successfully", departmentService.getAllDepartments()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<DepartmentResDto>> getDepartmentById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Department fetched successfully", departmentService.getDepartmentById(id)));
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<DepartmentResDto>>> getDepartmentsByDepartmentNameContainingIgnoreCase(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Department fetched successfully", departmentService.getDepartmentsByDepartmentNameContainingIgnoreCase(name)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<DepartmentResDto>> addDepartment(@RequestBody @Validated(OnCreate.class) DepartmentReqDto departmentReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Department added successfully", departmentService.addDepartment(departmentReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<DepartmentResDto>> updateDepartment(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) DepartmentReqDto departmentReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Department updated successfully", departmentService.updateDepartment(id, departmentReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable UUID id) {
        String res = "Department not deleted";
        if (departmentService.deleteDepartment(id)){
            res = "Department deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}

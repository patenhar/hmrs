package com.hrms.backend.services;

import com.hrms.backend.dtos.request.DepartmentReqDto;
import com.hrms.backend.dtos.response.DepartmentResDto;
import com.hrms.backend.entities.Department;
import com.hrms.backend.repos.DepartmentRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;
    private final ModelMapper modelMapper;

    public DepartmentService(DepartmentRepo departmentRepo, ModelMapper modelMapper) {
        this.departmentRepo = departmentRepo;
        this.modelMapper = modelMapper;
    }

    public DepartmentResDto findDepartmentById(UUID id) {
        Department department = departmentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return modelMapper.map(department, DepartmentResDto.class);
    }

    public List<DepartmentResDto> getAllDepartments() {
        return departmentRepo.findAll().stream().map(st -> modelMapper.map(st, DepartmentResDto.class)).toList();
    }

    public List<DepartmentResDto> getDepartmentsByDepartmentNameContainingIgnoreCase(String name) {
        return departmentRepo.findDepartmentsByDepartmentNameContainingIgnoreCase(name).stream().map(st -> modelMapper.map(st, DepartmentResDto.class)).toList();
    }

    public DepartmentResDto getDepartmentById(UUID id) {
        return findDepartmentById(id);
    }

    public DepartmentResDto addDepartment(DepartmentReqDto departmentReqDto) {
        Department department =  departmentRepo.save(modelMapper.map(departmentReqDto, Department.class));
        return modelMapper.map(department, DepartmentResDto.class);
    }

    public DepartmentResDto updateDepartment(UUID id, DepartmentReqDto departmentReqDto) {
        DepartmentResDto department = findDepartmentById(id);
        modelMapper.map(departmentReqDto, department);
        Department updatedDepartment =  departmentRepo.save(modelMapper.map(departmentReqDto, Department.class));
        return modelMapper.map(updatedDepartment, DepartmentResDto.class);
    }

    public boolean deleteDepartment(UUID id) {
        findDepartmentById(id);
        departmentRepo.deleteById(id);
        return true;
    }
}

package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ExpenseTypeReqDto;
import com.hrms.backend.dtos.response.ExpenseTypeResDto;
import com.hrms.backend.entities.ExpenseType;
import com.hrms.backend.repos.ExpenseTypeRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExpenseTypeService {
    private final ExpenseTypeRepo expenseTypeRepo;
    private final ModelMapper modelMapper;

    public ExpenseTypeService(ExpenseTypeRepo expenseTypeRepo, ModelMapper modelMapper) {
        this.expenseTypeRepo = expenseTypeRepo;
        this.modelMapper = modelMapper;
    }

    public ExpenseTypeResDto findExpenseTypeById(UUID id) {
        ExpenseType expenseType = expenseTypeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense type is not found"));
        return modelMapper.map(expenseType, ExpenseTypeResDto.class);
    }

    public List<ExpenseTypeResDto> getAllExpenseTypes() {
        return expenseTypeRepo.findAll().stream().map(st -> modelMapper.map(st, ExpenseTypeResDto.class)).toList();
    }

    public List<ExpenseTypeResDto> getExpenseTypesByExpenseTypeNameContainingIgnoreCase(String name) {
        return expenseTypeRepo.findExpenseTypesByExpenseTypeNameContainingIgnoreCase(name).stream().map(st -> modelMapper.map(st, ExpenseTypeResDto.class)).toList();
    }

    public ExpenseTypeResDto getExpenseTypeById(UUID id) {
        return findExpenseTypeById(id);
    }

    public ExpenseTypeResDto addExpenseType(ExpenseTypeReqDto expenseTypeReqDto) {
        ExpenseType expenseType =  expenseTypeRepo.save(modelMapper.map(expenseTypeReqDto, ExpenseType.class));
        return modelMapper.map(expenseType, ExpenseTypeResDto.class);
    }

    public ExpenseTypeResDto updateExpenseType(UUID id, ExpenseTypeReqDto expenseTypeReqDto) {
        ExpenseTypeResDto expenseType = findExpenseTypeById(id);
        modelMapper.map(expenseTypeReqDto, expenseType);
        ExpenseType updatedExpenseType =  expenseTypeRepo.save(modelMapper.map(expenseTypeReqDto, ExpenseType.class));
        return modelMapper.map(updatedExpenseType, ExpenseTypeResDto.class);
    }

    public boolean deleteExpenseType(UUID id) {
        findExpenseTypeById(id);
        expenseTypeRepo.deleteById(id);
        return true;
    }
}

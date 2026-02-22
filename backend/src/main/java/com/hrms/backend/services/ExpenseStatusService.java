package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ExpenseStatusReqDto;
import com.hrms.backend.dtos.response.ExpenseStatusResDto;
import com.hrms.backend.entities.ExpenseStatus;
import com.hrms.backend.entities.ExpenseStatus;
import com.hrms.backend.repos.ExpenseStatusRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExpenseStatusService {

    private final ExpenseStatusRepo expenseStatusRepo;
    private final ModelMapper modelMapper;

    public ExpenseStatusService(ExpenseStatusRepo expenseStatusRepo, ModelMapper modelMapper) {
        this.expenseStatusRepo = expenseStatusRepo;
        this.modelMapper = modelMapper;
    }

    public ExpenseStatusResDto findExpenseStatusById(UUID id) {
        ExpenseStatus expenseStatus = expenseStatusRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense status not found"));
        return modelMapper.map(expenseStatus, ExpenseStatusResDto.class);
    }

    public List<ExpenseStatusResDto> getAllExpenseStatuses() {
        return expenseStatusRepo.findAll().stream().map(st -> modelMapper.map(st, ExpenseStatusResDto.class)).toList();
    }

    public ExpenseStatusResDto getExpenseStatusById(UUID id) {
        return findExpenseStatusById(id);
    }

    public ExpenseStatusResDto addExpenseStatus(ExpenseStatusReqDto expenseStatusReqDto) {
        ExpenseStatus expenseStatus = expenseStatusRepo.save(modelMapper.map(expenseStatusReqDto, ExpenseStatus.class));
        return modelMapper.map(expenseStatus, ExpenseStatusResDto.class);
    }

    public ExpenseStatusResDto updateExpenseStatus(UUID id, ExpenseStatusReqDto expenseStatusReqDto) {
        ExpenseStatusResDto expenseStatus = findExpenseStatusById(id);
        modelMapper.map(expenseStatusReqDto, expenseStatus);
        ExpenseStatus updatedExpenseStatus = expenseStatusRepo.save(modelMapper.map(expenseStatusReqDto, ExpenseStatus.class));
        return modelMapper.map(updatedExpenseStatus, ExpenseStatusResDto.class);
    }

    public boolean deleteExpenseStatus(UUID id) {
        findExpenseStatusById(id);
        expenseStatusRepo.deleteById(id);
        return true;
    }
}

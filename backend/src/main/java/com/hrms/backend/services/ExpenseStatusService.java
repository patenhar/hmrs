package com.hrms.backend.services;

import com.hrms.backend.dtos.response.ExpenseStatusResDto;
import com.hrms.backend.entities.ExpenseStatus;
import com.hrms.backend.repos.ExpenseStatusRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}

package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.ExpenseReqDto;
import com.hrms.backend.entities.Expense;
import com.hrms.backend.utils.ApiResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IExpenseService {
    ApiResponse<List<Expense>> getAllExpenses();

    ApiResponse<Expense> getExpenseById(UUID id);

    ApiResponse<List<Expense>> getExpenseByUserId(UUID id);

    ApiResponse<List<Expense>> getExpenseByTravelId(UUID id);

    ApiResponse<List<Expense>> getExpenseByUserTravelId(UUID id);

    ApiResponse<Expense> addExpense(ExpenseReqDto expenseReqDto) throws IOException;

    ApiResponse<Expense> updateExpense(UUID id, ExpenseReqDto expenseReqDto);

    ApiResponse<String> deleteExpense(UUID id);
}

package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ExpenseReqDto;
import com.hrms.backend.dtos.response.ExpenseResDto;
import com.hrms.backend.entities.Expense;
import com.hrms.backend.services.ExpenseService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<ExpenseResDto>>> getAllExpenses() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All expenses fetched successfully", expenseService.getAllExpenses()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<ExpenseResDto>> getExpenseById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense fetched successfully", expenseService.getExpenseById(id)));
    }

    @GetMapping("/user/{id}")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<ExpenseResDto>>> getExpenseByUserId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All user expenses fetched successfully", expenseService.getExpenseByUserId(id)));
    }

    @PatchMapping(path = "/{id}/approve")
    public ResponseEntity<ApiResponse<List<String>>> approveExpense(@PathVariable UUID id) {
        expenseService.approveExpense(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense approved successfully", null));
    }

    @PatchMapping(path = "/{id}/reject")
    public ResponseEntity<ApiResponse<List<String>>> rejectExpense(@PathVariable UUID id, @RequestBody String remark) {
        expenseService.rejectExpense(id, remark);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense rejected successfully", null));
    }



    @PostMapping(path = "/", consumes = "multipart/form-data")
//    @PreAuthorize("hasAuthority('ADD_TRAVEL')")
    public ResponseEntity<ApiResponse<ExpenseResDto>> addExpense(@Validated(OnCreate.class) @ModelAttribute ExpenseReqDto expenseDto) throws Exception {
        String res = "Expense could not be added";
        if (expenseService.addExpense(expenseDto)) {
            res = "Expense added successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>( res, null ));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_TRAVEL')")
    public ResponseEntity<ApiResponse<ExpenseResDto>> updateExpense(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) ExpenseReqDto expenseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All expenses fetched successfully", expenseService.updateExpense(id, expenseDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_TRAVEL')")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable UUID id) {
        String res = "Expense could not be deleted";
        if(expenseService.deleteExpense(id)){
            res = "Expense deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }
}

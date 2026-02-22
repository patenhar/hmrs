package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ExpenseStatusReqDto;
import com.hrms.backend.dtos.response.ExpenseStatusResDto;
import com.hrms.backend.entities.ExpenseStatus;
import com.hrms.backend.services.ExpenseStatusService;
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
@RequestMapping("api/expense-statuses")
public class ExpenseStatusController {
    private final ExpenseStatusService expenseStatusService;

    @Autowired
    public ExpenseStatusController(ExpenseStatusService expenseStatusService) {
        this.expenseStatusService = expenseStatusService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<ExpenseStatusResDto>>> getAllExpenseStatuses() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All expense statuses fetched successfully", expenseStatusService.getAllExpenseStatuses()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<ExpenseStatusResDto>> getExpenseStatusById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense Status fetched successfully", expenseStatusService.getExpenseStatusById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<ExpenseStatusResDto>> addExpenseStatus(@RequestBody @Validated(OnCreate.class) ExpenseStatusReqDto expenseStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense Status added successfully", expenseStatusService.addExpenseStatus(expenseStatusReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<ExpenseStatusResDto>> updateExpenseStatus(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) ExpenseStatusReqDto expenseStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense Status updated successfully", expenseStatusService.updateExpenseStatus(id, expenseStatusReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteExpenseStatus(@PathVariable UUID id) {
        String res = "Expense Status not deleted";
        if (expenseStatusService.deleteExpenseStatus(id)){
            res = "Expense Status deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}

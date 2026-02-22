package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.ExpenseTypeReqDto;
import com.hrms.backend.dtos.response.ExpenseTypeResDto;
import com.hrms.backend.services.ExpenseTypeService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/expense-types")
public class ExpenseTypeController {
    private final ExpenseTypeService expenseTypeService;

    @Autowired
    public ExpenseTypeController(ExpenseTypeService expenseTypeService) {
        this.expenseTypeService = expenseTypeService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<ExpenseTypeResDto>>> getAllExpenseTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All expense types fetched successfully", expenseTypeService.getAllExpenseTypes()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<ExpenseTypeResDto>> getExpenseTypeById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense type fetched successfully", expenseTypeService.getExpenseTypeById(id)));
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<ExpenseTypeResDto>>> getExpenseTypesByExpenseTypeNameContainingIgnoreCase(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense type fetched successfully", expenseTypeService.getExpenseTypesByExpenseTypeNameContainingIgnoreCase(name)));
    }



    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<ExpenseTypeResDto>> addExpenseType(@RequestBody @Validated(OnCreate.class) ExpenseTypeReqDto expenseTypeReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense type added successfully", expenseTypeService.addExpenseType(expenseTypeReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<ExpenseTypeResDto>> updateExpenseType(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) ExpenseTypeReqDto expenseTypeReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Expense type updated successfully", expenseTypeService.updateExpenseType(id, expenseTypeReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteExpenseType(@PathVariable UUID id) {
        String res = "Expense type not deleted";
        if (expenseTypeService.deleteExpenseType(id)){
            res = "Expense type deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}

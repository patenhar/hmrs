package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ExpenseReqDto;
import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.Expense;
import com.hrms.backend.entities.ExpenseType;
import com.hrms.backend.entities.UserTravel;
import com.hrms.backend.repos.ExpenseRepo;
import com.hrms.backend.repos.ExpenseTypeRepo;
import com.hrms.backend.repos.UserTravelRepo;
import com.hrms.backend.services.interfaces.IExpenseService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService implements IExpenseService {
    private final ExpenseRepo expenseRepo;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final UserTravelRepo userTravelRepo;
    private final ExpenseTypeRepo expenseTypeRepo;
    private final ExpenseTypeService expenseTypeService;

    public ExpenseService(ExpenseRepo expenseRepo, ModelMapper modelMapper, DocumentService documentService, UserTravelRepo userTravelRepo, ExpenseTypeRepo expenseTypeRepo, ExpenseTypeService expenseTypeService) {
        this.expenseRepo = expenseRepo;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.userTravelRepo = userTravelRepo;
        this.expenseTypeRepo = expenseTypeRepo;
        this.expenseTypeService = expenseTypeService;
    }
    private Expense findById(UUID id) {
        return expenseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    @Override
    public ApiResponse<List<Expense>> getAllExpenses() {
        return new ApiResponse<>("All travels fetched successfully", expenseRepo.findAll());
    }

    @Override
    public ApiResponse<Expense> getExpenseById(UUID id) {
        return new ApiResponse<>("Expense fetched successfully", findById(id));
    }

    @Override
    public ApiResponse<List<Expense>> getExpenseByUserId(UUID id) {
        return new ApiResponse<>("Fetched user expenses", expenseRepo.findExpensesByUserId(id));
    }

    @Override
    public ApiResponse<List<Expense>> getExpenseByTravelId(UUID id) {
        return new ApiResponse<>("Fetched travel expenses", expenseRepo.findExpensesByTravelId(id));
    }

    @Override
    public ApiResponse<List<Expense>> getExpenseByUserTravelId(UUID id) {
        return new ApiResponse<>("Successfully fetched expenses for provided user and travel", expenseRepo.findExpensesByUserTravelId(id));
    }

    @Override
    @Transactional
    public ApiResponse<Expense> addExpense(ExpenseReqDto expenseReqDto) {
        UserTravel userTravel = userTravelRepo.findById(expenseReqDto.getUserTravelId()).orElseThrow(() -> new ResourceNotFoundException("User travel not found"));
        if (LocalDate.now().isAfter(userTravel.getTravel().getReturnDate().plusDays(10))){
            return new ApiResponse<>("You are not allowed to upload expense after 10 days of return date", null);
        }
        Expense expense = modelMapper.map(expenseReqDto, Expense.class);
        ApiResponse<Document> response = documentService.uploadTravelDocument(expenseReqDto.getTravelDocumentReqDto());
        expense.setDocument(response.getData());
        expense.setUserTravel(userTravel);
        expense.setExpenseType(modelMapper.map(expenseTypeService.findExpenseTypeById(expenseReqDto.getExpenseTypeId()), ExpenseType.class));
//        expense.setExpenseStatus("");
        expenseRepo.save(expense);
        return new ApiResponse<>("Expense saved successfully", null);
    }

    @Override
    public ApiResponse<Expense> updateExpense(UUID id, ExpenseReqDto expenseReqDto) {
        return null;
    }

    @Override
    public ApiResponse<String> deleteExpense(UUID id) {
        Expense expense = findById(id);
        if (expense.getExpenseStatus().getExpenseStatusName().equals("Approved")) {
            return new ApiResponse<>("This operation is now not allowed", null);
        }
        expenseRepo.deleteById(id);
        return new ApiResponse<>("Expense deleted successfully", null);
    }
}

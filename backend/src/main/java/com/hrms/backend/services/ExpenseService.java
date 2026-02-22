package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ExpenseReqDto;
import com.hrms.backend.dtos.response.DocumentTypeResDto;
import com.hrms.backend.dtos.response.ExpenseResDto;
import com.hrms.backend.dtos.response.UserTravelResDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.ExpenseRepo;
import com.hrms.backend.repos.ExpenseTypeRepo;
import com.hrms.backend.repos.UserTravelRepo;
import com.hrms.backend.services.interfaces.IExpenseService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final ExpenseTypeService expenseTypeService;
    private final UserTravelService userTravelService;
    private final ExpenseStatusService expenseStatusService;

    public ExpenseService(ExpenseRepo expenseRepo, ModelMapper modelMapper, DocumentService documentService, ExpenseTypeService expenseTypeService, UserTravelService userTravelService, ExpenseStatusService expenseStatusService) {
        this.expenseRepo = expenseRepo;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.expenseTypeService = expenseTypeService;
        this.userTravelService = userTravelService;
        this.expenseStatusService = expenseStatusService;
    }
    private ExpenseResDto findExpenseById(UUID id) {
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return modelMapper.map(expense, ExpenseResDto.class);
    }

    public List<ExpenseResDto> getAllExpenses() {
        return expenseRepo.findAll().stream().map(e -> modelMapper.map(e, ExpenseResDto.class)).toList();
    }

    public ExpenseResDto getExpenseById(UUID id) {
        return findExpenseById(id);
    }

    public List<ExpenseResDto> getExpenseByUserId(UUID id) {
        return expenseRepo.findExpensesByUserId(id).stream().map(e -> modelMapper.map(e, ExpenseResDto.class)).toList();
    }

    public List<ExpenseResDto> getExpenseByTravelId(UUID id) {
        return expenseRepo.findExpensesByTravelId(id).stream().map(e -> modelMapper.map(e, ExpenseResDto.class)).toList();
    }

    public List<ExpenseResDto> getExpenseByUserTravelId(UUID id) {
        return expenseRepo.findExpensesByUserTravelId(id).stream().map(e -> modelMapper.map(e, ExpenseResDto.class)).toList();
    }

    @Transactional
    public boolean addExpense(ExpenseReqDto expenseReqDto) throws IOException {
        UserTravelResDto userTravelResDto = userTravelService.findUserTravelById(expenseReqDto.getUserTravelId());
        if (LocalDate.now().isAfter(userTravelResDto.getTravel().getReturnDate().plusDays(10))){
            return false;
        }
        Expense expense = modelMapper.map(expenseReqDto, Expense.class);
        expense.setDocument(documentService.uploadDocument(expenseReqDto.getDocumentReqDto()));
        expense.setUserTravel(modelMapper.map(userTravelResDto, UserTravel.class));
        expense.setExpenseType(modelMapper.map(expenseTypeService.findExpenseTypeById(expenseReqDto.getExpenseTypeId()), ExpenseType.class));
        expense.setExpenseStatus(modelMapper.map(expenseStatusService.findExpenseStatusById(UUID.fromString("c114f8cc-697e-4072-ba46-c797896e16ee")), ExpenseStatus.class));
        expenseRepo.save(expense);
        return true;
    }
    public Expense approveExpense(UUID id) {
        Expense expense = modelMapper.map(findExpenseById(id), Expense.class);
        expense.setExpenseStatus(modelMapper.map(expenseStatusService.findExpenseStatusById(UUID.fromString("f52bb348-748a-463f-a8d4-64e6100b5dcf")), ExpenseStatus.class));
        return expenseRepo.save(expense);
    }
    public Expense rejectExpense(UUID id) {
        Expense expense = modelMapper.map(findExpenseById(id), Expense.class);
        expense.setExpenseStatus(modelMapper.map(expenseStatusService.findExpenseStatusById(UUID.fromString("705899b0-d256-4bae-8774-5e300f858509")), ExpenseStatus.class));
        return expenseRepo.save(expense);
    }

    public ExpenseResDto updateExpense(UUID id, ExpenseReqDto expenseReqDto) {
        return null;
    }

    public boolean deleteExpense(UUID id) {
        ExpenseResDto expenseResDto = findExpenseById(id);
        if (expenseResDto.getExpenseStatus().getExpenseStatusName().equals("Approved")) {
            return false;
        }
        expenseRepo.deleteById(id);
        return true;
    }
}

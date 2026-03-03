package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ExpenseInDto;
import com.hrms.backend.dtos.request.ExpenseReqDto;
import com.hrms.backend.dtos.response.ExpenseResDto;
import com.hrms.backend.dtos.response.UserTravelResDto;
import com.hrms.backend.dtos.spec.ExpenseSpecDto;
import com.hrms.backend.dtos.spec.SortDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.enums.ExpenseStatus;
import com.hrms.backend.repos.ExpenseRepo;
import com.hrms.backend.services.interfaces.IExpenseService;
import com.hrms.backend.utils.ExpenseSpecification;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ExpenseService {
    private static final String HR_ROLE = "HR";

    private final ExpenseRepo expenseRepo;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final UserTravelService userTravelService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public ExpenseService(ExpenseRepo expenseRepo, ModelMapper modelMapper, DocumentService documentService, UserTravelService userTravelService, UserService userService, NotificationService notificationService, EmailService emailService) {
        this.expenseRepo = expenseRepo;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.userTravelService = userTravelService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.emailService = emailService;
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
    public boolean addExpense(ExpenseReqDto expenseReqDto) throws IOException, Exception {
        UserTravelResDto userTravelResDto = userTravelService.findUserTravelById(expenseReqDto.getUserTravelId());
        if (LocalDate.now().isAfter(userTravelResDto.getTravel().getReturnDate().plusDays(10))){
            throw new Exception("Expense submission period has ended");
        }
        List<ExpenseResDto> expenses = getExpenseByUserTravelId(expenseReqDto.getUserTravelId());
        double totalAmount = expenses.stream().mapToDouble(e -> e.getAmount()).sum() + expenseReqDto.getAmount();
        if (totalAmount > userTravelResDto.getTravel().getMaxGrantPerDay()) {
            throw new Exception("You cannot apply more than limit");
        }
        Expense expense = modelMapper.map(expenseReqDto, Expense.class);
        expense.setDocument(documentService.uploadDocument(expenseReqDto.getDocumentReqDto()));
        expense.setUserTravel(userTravelService.findUserTravelEntityById(expenseReqDto.getUserTravelId()));
        expense.setExpenseType(expenseReqDto.getExpenseType());
        expense.setExpenseStatus(ExpenseStatus.PENDING);
        expenseRepo.save(expense);

        User submittedBy = userService.getAuthenticatedUser();
        String expenseTitle = "New Expense Submitted";
        String expenseDesc = "A new expense of " + expenseReqDto.getAmount()
                + " has been submitted by " + submittedBy.getEmail()
                + " for: " + expenseReqDto.getDescription() + ".";
        userService.getUsersByRoleName(HR_ROLE).forEach(hr -> {
            notificationService.createNotification(expenseTitle, expenseDesc, hr.getPkUserId());
            emailService.sendMail(hr.getEmail(), expenseTitle, expenseDesc);
        });

        return true;
    }
    public Expense approveExpense(UUID id) {
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expense.setExpenseStatus(ExpenseStatus.APPROVED);
        expense.setLastActionAt(LocalDateTime.now());
        expense.setLastActionBy(userService.getAuthenticatedUser());
        Expense saved = expenseRepo.save(expense);

        if (saved.getUserTravel() != null && saved.getUserTravel().getUser() != null) {
            User owner = saved.getUserTravel().getUser();
            String title = "Expense Approved";
            String desc = "Your expense of " + saved.getAmount()
                    + " for '" + saved.getDescription() + "' has been approved.";
            notificationService.createNotification(title, desc, owner.getPkUserId());
            emailService.sendMail(owner.getEmail(), title, desc);
        }
        return saved;
    }

    public Expense rejectExpense(UUID id, String remarks) {
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expense.setExpenseStatus(ExpenseStatus.REJECTED);
        expense.setLastActionAt(LocalDateTime.now());
        expense.setLastActionBy(userService.getAuthenticatedUser());
        if (remarks == null || remarks.isEmpty()) {
            expense.setRemarks("No remarks provided");
        } else {
            expense.setRemarks(remarks);
        }
        Expense saved = expenseRepo.save(expense);

        if (saved.getUserTravel() != null && saved.getUserTravel().getUser() != null) {
            User owner = saved.getUserTravel().getUser();
            String title = "Expense Rejected";
            String desc = "Your expense of " + saved.getAmount()
                    + " for '" + saved.getDescription() + "' has been rejected."
                    + (saved.getRemarks() != null ? " Remarks: " + saved.getRemarks() : "");
            notificationService.createNotification(title, desc, owner.getPkUserId());
            emailService.sendMail(owner.getEmail(), title, desc);
        }
        return saved;
    }

    public ExpenseResDto updateExpense(UUID id, ExpenseReqDto expenseReqDto) {
        return null;
    }

    private String mapSortField(String field) {
        return switch (field) {
            case "expenseStatusName" -> "expenseStatus";
            case "expenseTypeName"   -> "expenseType";
            case "actorEmail"        -> "lastActionBy.email";
            default -> field;
        };
    }

    public boolean deleteExpense(UUID id) {
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        if (expense.getExpenseStatus() == ExpenseStatus.APPROVED) {
            return false;
        }
        expenseRepo.deleteById(id);
        return true;
    }

    public Page<ExpenseResDto> searchExpensesWithPaginationSortingAndFiltering(ExpenseInDto expenseInDto) {
        ExpenseSpecDto specDto = ExpenseSpecDto.builder()
                .userTravelId(expenseInDto.getUserTravelId())
                .description(expenseInDto.getDescription())
                .type(expenseInDto.getType())
                .status(expenseInDto.getStatus())
                .actor(expenseInDto.getActor())
                .amountMin(expenseInDto.getAmountMin())
                .amountMax(expenseInDto.getAmountMax())
                .build();

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(expenseInDto.getSort());
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                String field = mapSortField(sortDto.getField());
                orders.add(new Sort.Order(direction, field));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                expenseInDto.getPage(),
                expenseInDto.getSize(),
                Sort.by(orders)
        );

        Specification<Expense> specification = ExpenseSpecification.getSpecification(specDto);
        Page<Expense> expenses = expenseRepo.findAll(specification, pageRequest);
        return expenses.map(e -> modelMapper.map(e, ExpenseResDto.class));
    }
}

package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.TravelInDto;
import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.dtos.request.ExpenseInDto;
import com.hrms.backend.dtos.response.ExpenseResDto;
import com.hrms.backend.dtos.response.TravelResDto;
import com.hrms.backend.dtos.response.UserTravelResDtoForTravel;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.services.ExpenseService;
import com.hrms.backend.services.TravelService;
import com.hrms.backend.services.UserTravelService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/travels")
public class TravelController {
    private final TravelService travelService;
    private final UserTravelService userTravelService;
    private final ExpenseService expenseService;

    @Autowired
    public TravelController(TravelService travelService, UserTravelService userTravelService, ExpenseService expenseService) {
        this.travelService = travelService;
        this.userTravelService = userTravelService;
        this.expenseService = expenseService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<List<TravelResDto>>> getAllTravels() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All travels fetched successfully", travelService.getAllTravels()));
    }

    @GetMapping("/filtering&pagination&sorting")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<Page<TravelResDto>> test(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "[{\"field\":\"travelDate\",\"direction\":\"desc\"}]") String sort,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "travel-date", required = false) String travelDate,
            @RequestParam(name = "return-date", required = false) String returnDate,
            @RequestParam(name = "grant-limit", required = false) String maxGrantPerDay,
            @RequestParam(name = "grant-limit-min", required = false) String maxGrantPerDayMin,
            @RequestParam(name = "grant-limit-max", required = false) String maxGrantPerDayMax,
            @RequestParam(name = "hr-mail", required = false) String hrMail
            ) {
            String travelDateValue = normalizeOptionalString(travelDate);
            String returnDateValue = normalizeOptionalString(returnDate);

            LocalDate parsedTravelDate = parseOptionalDate(travelDateValue);
            LocalDate parsedReturnDate = parseOptionalDate(returnDateValue);
        Double parsedMaxGrantPerDay = parseOptionalDouble(maxGrantPerDay, "grant-limit");
        Double parsedMaxGrantPerDayMin = parseOptionalDouble(normalizeOptionalString(maxGrantPerDayMin), "grant-limit-min");
        Double parsedMaxGrantPerDayMax = parseOptionalDouble(normalizeOptionalString(maxGrantPerDayMax), "grant-limit-max");

        Page<TravelResDto> travels = travelService.searchTravelWithPaginationSortingAndFiltering(
                TravelInDto.builder()
                        .title(title)
                        .description(description)
                        .hrMail(hrMail)
                        .travelDate(parsedTravelDate)
                        .returnDate(parsedReturnDate)
                    .travelDateKeyword(parsedTravelDate == null ? travelDateValue : null)
                    .returnDateKeyword(parsedReturnDate == null ? returnDateValue : null)
                        .maxGrantPerDay(parsedMaxGrantPerDay)
                        .maxGrantPerDayMin(parsedMaxGrantPerDayMin)
                        .maxGrantPerDayMax(parsedMaxGrantPerDayMax)
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build());

        return ResponseEntity.ok(travels);
    }

    private String normalizeOptionalString(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private LocalDate parseOptionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private Double parseOptionalDouble(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be a valid number");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<TravelResDto>> getTravelById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel fetched successfully", travelService.getTravelById(id)));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<List<TravelResDto>>> getTravelByUserId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel fetched for user successfully", travelService.getTravelByUserId(id)));
    }

    @GetMapping("/users/{userTravelId}/expenses")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<List<ExpenseResDto>>> getExpenseByUserId(@PathVariable UUID userTravelId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All user expenses fetched successfully", expenseService.getExpenseByUserTravelId(userTravelId)));
    }

    @GetMapping("/users/{userTravelId}/expenses/filtering&pagination&sorting")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<Page<ExpenseResDto>> getExpensesPaginated(
            @PathVariable UUID userTravelId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", defaultValue = "[{\"field\":\"amount\",\"direction\":\"desc\"}]") String sort,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "actor", required = false) String actor,
            @RequestParam(name = "amount-min", required = false) String amountMin,
            @RequestParam(name = "amount-max", required = false) String amountMax
    ) {
        Double parsedAmountMin = parseOptionalDouble(normalizeOptionalString(amountMin), "amount-min");
        Double parsedAmountMax = parseOptionalDouble(normalizeOptionalString(amountMax), "amount-max");

        Page<ExpenseResDto> expenses = expenseService.searchExpensesWithPaginationSortingAndFiltering(
                ExpenseInDto.builder()
                        .userTravelId(userTravelId)
                        .description(normalizeOptionalString(description))
                        .type(normalizeOptionalString(type))
                        .status(normalizeOptionalString(status))
                        .actor(normalizeOptionalString(actor))
                        .amountMin(parsedAmountMin)
                        .amountMax(parsedAmountMax)
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build());

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}/users")
    @PreAuthorize("hasAuthority('VIEW_TRAVEL') or hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<List<UserTravelResDtoForTravel>>> getTravelUsers(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel fetched for user successfully", userTravelService.getTravelUsers(id)));
    }
    @PostMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<TravelResDto>> addTravel(@RequestBody TravelReqDto travelDto) {
        String res = "Travel could not be added";
        if(travelService.addTravel(travelDto)) {
            res = "Travel added successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<TravelResDto>> updateTravel(@PathVariable UUID id, @RequestBody TravelReqDto travelDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel updated successfully", travelService.updateTravel(id, travelDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_TRAVEL')")
    public ResponseEntity<ApiResponse<String>> deleteTravel(@PathVariable UUID id) {
        String res = "Travel could not be deleted";
        if (travelService.deleteTravel(id)) {
            res = "Travel deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }
}

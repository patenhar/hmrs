package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnUpdate;
import com.hrms.backend.validations.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TravelReqDto {
    @NotBlank(message = "Title is required", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(message = "Description is required", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotBlank(message = "Travel date is required", groups = {OnCreate.class, OnUpdate.class})
    @Future(message = "Travel date must be in the future", groups = {OnCreate.class, OnUpdate.class})
    private Date travelDate;

    @NotBlank(message = "Return date is required", groups = {OnCreate.class, OnUpdate.class})
    @Future(message = "Return date must be in the future", groups = {OnCreate.class, OnUpdate.class})
    private Date returnDate;

    @NotNull(message = "Maximum grant per day is required", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "Grant amount cannot be less than zero", groups = {OnCreate.class, OnUpdate.class})
    private double maxGrantPerDay;

    @Email(message = "Invalid email format", groups = {OnCreate.class, OnUpdate.class})
    private String hrMail;

//    @Valid
//    private List<@org.hibernate.validator.constraints.UUID(message = "Invalid UUID format") UUID> destinationIds = new ArrayList<>();

    @NotEmpty(message = "At least one destination is required", groups = {OnCreate.class, OnUpdate.class})
    private List<@Valid AddressReqDto> destinations = new ArrayList<>();

    @NotEmpty(message = "At least one user is required", groups = {OnCreate.class, OnUpdate.class})
    @Valid
    private List<@org.hibernate.validator.constraints.UUID(message = "Invalid UUID format") UUID> userIds = new ArrayList<>();
}

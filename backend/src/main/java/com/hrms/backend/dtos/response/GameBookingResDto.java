package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.GameBookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameBookingResDto {
    private UUID pkGameBookingId;
    private GameSlotResDto gameSlot;
    private UserResDto bookedBy;
    private GameBookingStatus gameBookingStatus;
    private List<UserResDto> teamMembers;
}

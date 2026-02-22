package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.GameBookingStatus;
import com.hrms.backend.entities.GameSlot;
import com.hrms.backend.entities.User;
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
    private GameBookingStatusResDto gameBookingStatus;
    private List<UserResDto> teamMembers;
}

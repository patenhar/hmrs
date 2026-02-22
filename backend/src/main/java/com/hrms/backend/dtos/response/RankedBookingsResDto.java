package com.hrms.backend.dtos.response;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class RankedBookingsResDto {
    private int rank;
    private UUID pkGameBookingId;
    private long inversePriority;
}

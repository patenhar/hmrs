package com.hrms.backend.dtos.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSpecDto {
    private String gameName;
    private Integer maxPlayersMin;
    private Integer maxPlayersMax;
}

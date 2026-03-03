package com.hrms.backend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInDto {
    private String gameName;
    private Integer maxPlayersMin;
    private Integer maxPlayersMax;
    private Integer page;
    private Integer size;
    private String sort;
}

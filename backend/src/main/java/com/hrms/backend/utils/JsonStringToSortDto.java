package com.hrms.backend.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.backend.dtos.spec.SortDto;

import java.util.Collections;
import java.util.List;

public class JsonStringToSortDto {
    public static List<SortDto> jsonStringToSortDto(String jsonString) {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(jsonString, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}

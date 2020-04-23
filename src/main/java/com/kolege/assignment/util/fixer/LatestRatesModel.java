package com.kolege.assignment.util.fixer;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreType(value = false)
public class LatestRatesModel {

    private Boolean success;
    private String base;
    private String date;
    private Map<String, Double> rates;

}

package com.kolege.assignment.util.fixer;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreType(value = false)
public class SymbolsModel {

    private Boolean success;
    private Map<String, String> symbols;

}

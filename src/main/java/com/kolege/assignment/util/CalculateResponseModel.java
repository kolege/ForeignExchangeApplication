package com.kolege.assignment.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalculateResponseModel {
    private Double response;

    public CalculateResponseModel(Double response){
        this.response = response;
    }

}

package com.kolege.assignment.util;

import lombok.Data;

@Data
public class ServiceResult<T>{
    private T data;
    private Boolean success;
    private String message;

    public ServiceResult(){
        this.success = true;
    }

    public ServiceResult(T data) {
        this.success = true;
        this.data = data;
    }
}

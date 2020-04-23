package com.kolege.assignment.service;

import com.kolege.assignment.domain.Unit;
import com.kolege.assignment.util.CalculateResponseModel;
import com.kolege.assignment.util.ServiceResult;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CurrencyService {

    void saveUnits();

    ServiceResult<CalculateResponseModel> getExchangeRate(String source, String target);

    ServiceResult<CalculateResponseModel> convert(String source, String target, Double amount);

    ServiceResult<Object> getConversionList(Pageable pageable, Long id, Date date);

    ServiceResult<List<Unit>> getUnits();

}

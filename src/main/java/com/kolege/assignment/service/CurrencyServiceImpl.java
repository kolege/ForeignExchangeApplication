package com.kolege.assignment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolege.assignment.domain.Conversion;
import com.kolege.assignment.domain.QConversion;
import com.kolege.assignment.domain.Unit;
import com.kolege.assignment.repository.ConversionRepository;
import com.kolege.assignment.repository.UnitRepository;
import com.kolege.assignment.util.CalculateResponseModel;
import com.kolege.assignment.util.Extensions;
import com.kolege.assignment.util.ServiceResult;
import com.kolege.assignment.util.fixer.LatestRatesModel;
import com.kolege.assignment.util.fixer.SymbolsModel;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service("CurrencyService")
@ExtensionMethod({
        Extensions.class
})
public class CurrencyServiceImpl implements CurrencyService {


    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private FixerService fixerService;

    @Override
    @Transactional
    public void saveUnits() {
        Map<String, String> units = null;
        try {
            /*ServiceResult<SymbolsModel> symbolsModel = fixerService.getCurrencyCodes();
            if (symbolsModel.getSuccess())
                units = symbolsModel.getData().getSymbols();
            else throw new Exception();*/
            throw new Exception();
        } catch (Exception e) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {
            };
            InputStream inputStream = TypeReference.class.getResourceAsStream("/units.json");
            try {
                units = mapper.readValue(inputStream, typeReference);
            } catch (IOException ex) {
                System.out.println("Unable to save units: " + e.getMessage());
                return;
            }
        }
        List<Unit> unitList = new ArrayList<>();
        units.forEach((key, value) -> unitList.add(new Unit(value, key)));
        unitRepository.saveAll(unitList);
        System.out.println("Units Saved Successfully!");
    }

    @Override
    public ServiceResult<CalculateResponseModel> getExchangeRate(String source, String target) {
        ServiceResult<CalculateResponseModel> serviceResult = new ServiceResult<>();
        ServiceResult<Double> rateServiceResult = calculateExchangeRate(source, target);
        if (rateServiceResult.getSuccess()) {
            Double rate = rateServiceResult.getData();
            serviceResult.setData(new CalculateResponseModel(rate));
        } else {
            serviceResult.setSuccess(false);
            serviceResult.setMessage(rateServiceResult.getMessage());
        }
        return serviceResult;
    }

    @Override
    @Transactional
    public ServiceResult<CalculateResponseModel> convert(String source, String target, Double amount) {
        ServiceResult<CalculateResponseModel> serviceResult = new ServiceResult<>();
        ServiceResult<Double> rateServiceResult = calculateExchangeRate(source, target);
        if (rateServiceResult.getSuccess()) {
            Double rate = rateServiceResult.getData();
            Double response = (amount * rate).formatForResponse();
            Conversion conversion = new Conversion(source, target, amount, response, rate);
            conversionRepository.save(conversion);
            serviceResult.setData(new CalculateResponseModel(response));
        } else {
            serviceResult.setSuccess(false);
            serviceResult.setMessage(rateServiceResult.getMessage());
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Object> getConversionList(Pageable pageable, Long id, Date date) {
        ServiceResult<Object> serviceResult = new ServiceResult<>();
        try {
            QConversion qConversion = QConversion.conversion;
            BooleanExpression booleanExpression = qConversion.id.isNotNull();
            if (date != null) {
                ZonedDateTime startDate = date.toInstant().atZone(ZoneId.systemDefault());
                ZonedDateTime endDate = new Date(date.getTime() + (1000 * 60 * 60 * 24)).toInstant().atZone(ZoneId.systemDefault());
                booleanExpression = qConversion.createdDate.between(startDate, endDate);
            }
            if (id != null) {
                booleanExpression = qConversion.id.eq(id);
            }
            Object response;
            if (pageable != null)
                response = conversionRepository.findAll(booleanExpression, pageable);
            else
                response = conversionRepository.findAll(booleanExpression);
            serviceResult.setData(response);
        } catch (Exception e) {
            serviceResult.setSuccess(false);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<Unit>> getUnits() {
        ServiceResult<List<Unit>> serviceResult = new ServiceResult<>();
        try{
            List<Unit> unitList = unitRepository.findAll();
            serviceResult.setData(unitList);
        }catch (Exception e){
            serviceResult.setSuccess(false);
            serviceResult.setMessage(e.getMessage());
        }
        return serviceResult;
    }

    private ServiceResult<Double> calculateExchangeRate(String source, String to) {
        ServiceResult<Double> serviceResult = new ServiceResult<>();
        ServiceResult<LatestRatesModel> latestRatesModelServiceResult = fixerService.getLatestRates(source, to);
        if (latestRatesModelServiceResult.getSuccess()) {
            try{
                LatestRatesModel latestRatesModel = latestRatesModelServiceResult.getData();
                Double sourceRate = latestRatesModel.getRates().get(source);
                Double toRate = latestRatesModel.getRates().get(to);
                serviceResult.setData((toRate / sourceRate).formatForResponse());
            }catch (Exception e){
                serviceResult.setSuccess(false);
                serviceResult.setMessage("Unknown Currency Type");
            }
        } else {
            serviceResult.setSuccess(false);
            serviceResult.setMessage(latestRatesModelServiceResult.getMessage());
        }
        return serviceResult;
    }

}

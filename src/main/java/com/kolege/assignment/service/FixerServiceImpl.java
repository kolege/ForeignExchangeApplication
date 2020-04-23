package com.kolege.assignment.service;

import com.kolege.assignment.util.ServiceResult;
import com.kolege.assignment.util.fixer.FixerServicePaths;
import com.kolege.assignment.util.fixer.LatestRatesModel;
import com.kolege.assignment.util.fixer.SymbolsModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("FixerService")
public class FixerServiceImpl implements FixerService{

    @Value("${fixer.api.url}")
    String fixerApiUrl;

    @Value("${fixer.token}")
    public String token;

    @Override
    public ServiceResult<LatestRatesModel> getLatestRates(String source, String target){
        ServiceResult<LatestRatesModel> serviceResult = new ServiceResult<>();
        try {
            StringBuilder builder = getMainUriPart(FixerServicePaths.latest.getValue());
            builder.append("&symbols=").append(source).append(",").append(target);
            RestTemplate restTemplate = new RestTemplate();
            LatestRatesModel latestRatesModel = restTemplate.getForObject(builder.toString(), LatestRatesModel.class);
            if (latestRatesModel.getSuccess())
                serviceResult.setData(latestRatesModel);
            else{
                throw new Exception();
            }
        }catch (Exception e){
            serviceResult.setSuccess(false);
            serviceResult.setMessage("Some Error Occurred on fixer.io");
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<SymbolsModel> getCurrencyCodes() {
        ServiceResult<SymbolsModel> serviceResult = new ServiceResult<>();
        try {
            StringBuilder builder = getMainUriPart(FixerServicePaths.symbols.getValue());
            RestTemplate restTemplate = new RestTemplate();
            SymbolsModel symbolsModel = restTemplate.getForObject(builder.toString(), SymbolsModel.class);
            if (symbolsModel.getSuccess())
                serviceResult.setData(symbolsModel);
            else
                throw new Exception();
        }catch (Exception e){
            serviceResult.setSuccess(false);
            serviceResult.setMessage("Cannot Reach fixer.io");
        }
        return serviceResult;
    }

    private StringBuilder getMainUriPart(String specificUriPart){
        StringBuilder builder = new StringBuilder(fixerApiUrl).append(specificUriPart);
        builder.append("?").append("access_key=").append(token);
        return builder;
    }


}

package com.kolege.assignment.service;

import com.kolege.assignment.util.ServiceResult;
import com.kolege.assignment.util.fixer.LatestRatesModel;
import com.kolege.assignment.util.fixer.SymbolsModel;

public interface FixerService {

    public ServiceResult<LatestRatesModel> getLatestRates(String source, String target);

    public ServiceResult<SymbolsModel> getCurrencyCodes();

}

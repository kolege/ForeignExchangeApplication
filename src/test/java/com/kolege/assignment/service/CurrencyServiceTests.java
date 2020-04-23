package com.kolege.assignment.service;

import com.kolege.assignment.AssignmentApplication;
import com.kolege.assignment.domain.Conversion;
import com.kolege.assignment.repository.ConversionRepository;
import com.kolege.assignment.util.CalculateResponseModel;
import com.kolege.assignment.util.ServiceResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AssignmentApplication.class})
public class CurrencyServiceTests {
    @Autowired
    CurrencyService currencyService;
    @Autowired
    ConversionRepository conversionRepository;

    @Before
    public void setup(){
        Conversion conversion = new Conversion("USD", "EUR", 100D, 0.8, 80D);
        conversionRepository.save(conversion);
    }

    @Test
    public void getExchangeRateTest(){
        ServiceResult<CalculateResponseModel> serviceResult = currencyService.getExchangeRate("USD","EUR");
        Assert.assertTrue(serviceResult.getSuccess());
    }

    @Test
    public void convertTest(){
        ServiceResult<CalculateResponseModel> serviceResult = currencyService.convert("USD", "EUR", 532D);
        Assert.assertTrue(serviceResult.getSuccess());
    }

    @Test
    public void getConversionListTestForPageable(){
        Date date = new Date();
        date = new Date(date.getTime()-(1000 * 60 * 60 * 12));
        ServiceResult<Object> serviceResult = currencyService.getConversionList(PageRequest.of(0, 1), null, date);
        Assert.assertTrue(serviceResult.getSuccess());
        Page page = ((Page)serviceResult.getData());
        Conversion conversion = (Conversion) page.getContent().get(0);
        Assert.assertEquals(Long.valueOf(1), conversion.getId());
    }

    @Test
    public void getConversionListWithDate(){
        Date date = new Date();
        date = new Date(date.getTime()-(1000 * 60 * 60 * 12));
        ServiceResult<Object> serviceResult = currencyService.getConversionList(null, null, date);
        Assert.assertTrue(serviceResult.getSuccess());
        List<Conversion> conversionList = (List<Conversion>) serviceResult.getData();
        Assert.assertEquals(Long.valueOf(1), conversionList.get(0).getId());
    }

    @Test
    public void getConversionListWithId(){
        ServiceResult<Object> serviceResult = currencyService.getConversionList(null, 1L, null);
        Assert.assertTrue(serviceResult.getSuccess());
        List<Conversion> conversionList = (List<Conversion>) serviceResult.getData();
        Assert.assertEquals(Long.valueOf(1), conversionList.get(0).getId());
    }

    @Test
    public void getConversionListWithIdAndDate(){
        Date date = new Date();
        date = new Date(date.getTime()-(1000 * 60 * 60 * 12));
        ServiceResult<Object> serviceResult = currencyService.getConversionList(null, 1L, date);
        Assert.assertTrue(serviceResult.getSuccess());
        List<Conversion> conversionList = (List<Conversion>) serviceResult.getData();
        Assert.assertEquals(Long.valueOf(1), conversionList.get(0).getId());
    }

}

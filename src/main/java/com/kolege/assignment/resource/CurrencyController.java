package com.kolege.assignment.resource;

import com.kolege.assignment.domain.Unit;
import com.kolege.assignment.service.CurrencyService;
import com.kolege.assignment.util.CalculateResponseModel;
import com.kolege.assignment.util.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(value = "/home")
    public String home() {
        return "Welcome Home";
    }

    @GetMapping(value = "/exchangerate")
    public ResponseEntity<ServiceResult<CalculateResponseModel>> getExchangeRate(@RequestParam(value = "source") String source, @RequestParam(value = "target") String target) {
        ServiceResult<CalculateResponseModel> response = currencyService.getExchangeRate(source.trim().toUpperCase(), target.trim().toUpperCase());
        if (response.getSuccess())
            return ResponseEntity.ok(response);
        else if (response.getMessage().contains("fixer.io"))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping(value = "/convert")
    public ResponseEntity<ServiceResult<CalculateResponseModel>> convert(@RequestParam(value = "amount") Double amount,
                                                                         @RequestParam(value = "source") String source, @RequestParam(value = "target") String target) {
        ServiceResult<CalculateResponseModel> response = currencyService.convert(source.trim().toUpperCase(), target.trim().toUpperCase(), amount);
        if (response.getSuccess())
            return ResponseEntity.ok(response);
        else if (response.getMessage().contains("fixer.io"))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping(value = "/conversionlist")
    public ResponseEntity<ServiceResult<Object>> conversionListWithPaging(@RequestParam(value = "page", required = false) Integer page,
                                                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                                          @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                                                                          @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty,
                                                                          @RequestParam(value = "id", required = false) Long id,
                                                                          @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        if (id == null && date == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Add Id Or Date!"
            );
        }
        PageRequest pageRequest = null;
        if (page != null){
            pageRequest = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        }
        ServiceResult<Object> serviceResult = currencyService.getConversionList(pageRequest, id, date);
        if (serviceResult.getSuccess())
            return ResponseEntity.ok(serviceResult);
        else
            return ResponseEntity.badRequest().body(serviceResult);
    }

    @GetMapping(value = "/units")
    public ResponseEntity<ServiceResult<List<Unit>>> convert() {
        ServiceResult<List<Unit>> response = currencyService.getUnits();
        if (response.getSuccess())
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);
    }


}

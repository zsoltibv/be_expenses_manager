package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    private CurrencyService currencyService;
    @Autowired
    public CurrencyController(CurrencyService currencyService)
    {
        this.currencyService=currencyService;
    }
    @GetMapping("/currencies")
    public ResponseEntity<?> getCurrencies() {
        return new ResponseEntity<>(currencyService.getCurrencies(), HttpStatus.OK);
    }
}

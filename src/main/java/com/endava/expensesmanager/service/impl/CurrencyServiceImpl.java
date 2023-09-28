package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExchangeRatesDto;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class CurrencyServiceImpl implements CurrencyService {

@Value("${exchange-rates-api-enable}")
    private boolean flag;
@Value("${exchange-rates-api-key}")
private String apiKey;

    private WebClient webClient = WebClient.builder().build();

    private ExchangeRatesDto exchangeRatesDto;
    private HashMap<String, BigDecimal> exchangeRates=new HashMap<>();
    private List<String> currencies;
    @Scheduled(fixedRate = 7200000)
    public void getApiRates() {

        if(flag)
        {exchangeRatesDto = webClient.get()
                .uri("https://v6.exchangerate-api.com/v6/"+apiKey+"/latest/EUR")
                .retrieve()
                .bodyToMono(ExchangeRatesDto.class)
                .block();
        List<String> knownCurrencies = List.of("EUR", "RON", "USD");
        currencies=knownCurrencies;
        exchangeRatesDto.getConversionRates().entrySet().stream()
                .filter(e -> knownCurrencies.contains(e.getKey()))
                .forEach(entry -> exchangeRates.put(entry.getKey(), entry.getValue()));}
        else{
         exchangeRates.put("EUR",new BigDecimal("5"));
            exchangeRates.put("USD",new BigDecimal("4.5"));
            exchangeRates.put("RON",new BigDecimal("1"));
        }
        System.out.println(exchangeRates);
    }

    public List<Expense> changeCurrencyTo(String code, List<Expense> expenseList) {
        List<Expense> newExpenseList = new ArrayList<>();

        for (int i = 0; i < expenseList.size(); i++) {
            if (!Objects.equals(expenseList.get(i).getCurrency().getCode(), code)) {
                Expense expenseItem = expenseList.get(i);
                expenseItem.setAmount(expenseList.get(i).getAmount()
                        .multiply(exchangeRates.get(code))
                        .divide(exchangeRates.get(expenseList.get(i).getCurrency().getCode()), 6, RoundingMode.HALF_UP));
                expenseItem.getCurrency().setCode(code);
                newExpenseList.add(expenseItem);
            } else {
                newExpenseList.add(expenseList.get(i));
            }

        }
        return newExpenseList;
    }
    public List<String> getCurrencies()
    {
        return currencies;
    }
}
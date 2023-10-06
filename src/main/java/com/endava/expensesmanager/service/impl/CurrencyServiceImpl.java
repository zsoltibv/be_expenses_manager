package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExchangeRatesDto;
import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.repository.CurrencyRepository;
import com.endava.expensesmanager.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;


@Service
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository currencyRepository;
    public CurrencyServiceImpl(CurrencyRepository currencyRepository)
    { this.currencyRepository=currencyRepository;}


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
        currencies=new ArrayList<>(knownCurrencies);
        exchangeRatesDto.getConversionRates().entrySet().stream()
                .filter(e -> knownCurrencies.contains(e.getKey()))
                .forEach(entry -> exchangeRates.put(entry.getKey(), entry.getValue()));}
        else{
            currencies=new ArrayList<>();
            currencies.add("EUR");
            currencies.add("USD");
            currencies.add("RON");

         exchangeRates.put("EUR",new BigDecimal("0.2"));
            exchangeRates.put("USD",new BigDecimal("0.22"));
            exchangeRates.put("RON",new BigDecimal("1"));
        }
        System.out.println(exchangeRates);
    }

    public List<Expense> changeCurrencyTo(String code, List<Expense> expenseList) {

        for (int i = 0; i < expenseList.size(); i++) {
            if (!Objects.equals(expenseList.get(i).getCurrency().getCode(), code)) {

                expenseList.get(i).setAmount(expenseList.get(i).getAmount()
                        .multiply(exchangeRates.get(code))
                        .divide(exchangeRates.get(expenseList.get(i).getCurrency().getCode()), 6, RoundingMode.HALF_UP));


            }

        }
        return expenseList;
    }

    @Override
    public List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }


}

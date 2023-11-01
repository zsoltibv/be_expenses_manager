package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.CurrencyDto;
import com.endava.expensesmanager.model.dto.ExchangeRatesDto;
import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.mapper.CurrencyMapper;
import com.endava.expensesmanager.repository.CurrencyRepository;
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
    private final HashMap<String, BigDecimal> exchangeRates = new HashMap<>();
    private final CurrencyRepository currencyRepository;
    @Value("${exchange-rates-api-enable}")
    private boolean flag;
    @Value("${exchange-rates-api-key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder().build();


    private ExchangeRatesDto exchangeRatesDto;
    private List<String> currencies;
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(fixedRate = 7200000)
    public void getApiRates() {

        if (flag) {
            exchangeRatesDto = webClient.get()
                    .uri("https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/EUR")
                    .retrieve()
                    .bodyToMono(ExchangeRatesDto.class)
                    .block();
            List<String> knownCurrencies = List.of("EUR", "RON", "USD");
            currencies = new ArrayList<>(knownCurrencies);
            exchangeRatesDto.getConversionRates().entrySet().stream()
                    .filter(e -> knownCurrencies.contains(e.getKey()))
                    .forEach(entry -> exchangeRates.put(entry.getKey(), entry.getValue()));
        } else {
            currencies = new ArrayList<>();
            currencies.add("EUR");
            currencies.add("USD");
            currencies.add("RON");

            exchangeRates.put("EUR", new BigDecimal("0.2"));
            exchangeRates.put("USD", new BigDecimal("0.22"));
            exchangeRates.put("RON", new BigDecimal("1"));
        }
        System.out.println(exchangeRates);
    }


    public List<ExpenseDto> changeCurrencyTo(String code, List<ExpenseDto> expenseList) {
        for (ExpenseDto expense : expenseList) {
            if (!Objects.equals(currencyRepository.findById(expense.getCurrency().getCurrencyId()).get().getCode(), code)) {

                expense.setAmount(expense.getAmount()
                        .multiply(exchangeRates.get(code))
                        .divide(exchangeRates.get(currencyRepository.findById(expense.getCurrency().getCurrencyId()).get().getCode()), 6, RoundingMode.HALF_UP));
                expense.setCurrency(currencyRepository.findByCode(code));


            }
        }
        return expenseList;
    }


    public List<CurrencyDto> getCurrencies() {
        List<Currency> currencies1 = currencyRepository.findAll();
        List<CurrencyDto> currencies2 = new ArrayList<>();
        for (Currency currency : currencies1) {

            currencies2.add(CurrencyMapper.toCurrencyDto(currency));
        }
        return currencies2;

    }


}

package com.endava.expensesmanager.model.mapper;

import com.endava.expensesmanager.model.dto.CurrencyDto;
import com.endava.expensesmanager.model.entity.Currency;

public class CurrencyMapper {
    public static CurrencyDto toCurrencyDto(Currency currency)
    {
        CurrencyDto currencyDto= new CurrencyDto();
        currencyDto.setCode(currency.getCode());
        currencyDto.setCurrencyId(currency.getCurrencyId());
        return currencyDto;
    }
}

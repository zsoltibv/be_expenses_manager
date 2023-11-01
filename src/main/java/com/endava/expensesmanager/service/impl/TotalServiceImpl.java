package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.service.CurrencyService;
import com.endava.expensesmanager.service.ExpenseService;
import com.endava.expensesmanager.service.TotalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TotalServiceImpl implements TotalService {
    private final CurrencyService currencyService;
    private final ExpenseService expenseService;

    public TotalServiceImpl(CurrencyService currencyService, ExpenseService expenseService) {
        this.currencyService = currencyService;
        this.expenseService = expenseService;
    }


    public BigDecimal totalExpenseSum(int userId, LocalDateTime startDate, LocalDateTime endDate, String code) {
        List<ExpenseDto> expenses = currencyService.changeCurrencyTo(code, expenseService.getExpensesByBeginDateAndEndDate(startDate, endDate, userId));
        BigDecimal sum = BigDecimal.ZERO;
        for (ExpenseDto expens : expenses) {
            sum = sum.add(expens.getAmount());
        }
        return sum;
    }

    public Map<String, BigDecimal> totalExpenseCategory(int userId, LocalDateTime startDate, LocalDateTime endDate, String code) {
        List<ExpenseDto> expenses = currencyService.changeCurrencyTo(code, expenseService.getExpensesByBeginDateAndEndDate(startDate, endDate, userId));


        return expenseService.sortExpenses(expenses);
    }

    public List<Map<String, BigDecimal>> barchartData(int userId, LocalDateTime startDate, LocalDateTime endDate, String code) {
        List<List<ExpenseDto>> barChartList = expenseService.getExpensesByBeginDateAndEndDateSortedBy(startDate, endDate, userId);
        List<List<ExpenseDto>> currencyBarChartList = new ArrayList<>();
        List<Map<String, BigDecimal>> barchartListMap = new ArrayList<>();
        for (List<ExpenseDto> listItem : barChartList) {
            List<ExpenseDto> newListItem = currencyService.changeCurrencyTo(code, listItem);
            currencyBarChartList.add(newListItem);
        }
        for (List<ExpenseDto> listItem : currencyBarChartList) {
            Map<String, BigDecimal> mapItem = expenseService.sortExpenses(listItem);
            barchartListMap.add(mapItem);

        }
        return barchartListMap;
    }

}

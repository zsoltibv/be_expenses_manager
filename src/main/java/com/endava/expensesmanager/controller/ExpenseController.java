package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.CurrencyService;
import com.endava.expensesmanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
@Validated
public class ExpenseController {
    private final ExpenseService expenseService;
    private final CurrencyService currencyService;


    public ExpenseController(ExpenseService expenseService, CurrencyService currencyService) {
        this.expenseService = expenseService;
        this.currencyService = currencyService;
    }

    @PostMapping()
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody @Valid ExpenseDto expenseDto) {
        expenseService.addExpense(expenseDto);
        return new ResponseEntity<>(expenseDto, HttpStatus.CREATED);
    }

    @GetMapping("byUser/{userId}")
    public ResponseEntity<List<ExpenseDto>> getExpensesByUserId(@PathVariable Integer userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {

        List<ExpenseDto> expenses = expenseService.getExpensesByUserId(userId, startDate, endDate);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalSum(@RequestParam int userId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam String code) {
        List<Expense> expenses = currencyService.changeCurrencyTo(code, expenseService.getExpensesByDates(startDate, endDate, userId));
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < expenses.size(); i++) {
            sum = sum.add(expenses.get(i).getAmount());
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getTotalSumCategory(@RequestParam int userId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam String code) {
        if (endDate.isAfter(LocalDate.now()))
            return new ResponseEntity<>("The end date is invalid", HttpStatus.BAD_REQUEST);

        List<Expense> expenses = currencyService.changeCurrencyTo(code, expenseService.getExpensesByDates(startDate, endDate, userId));

        Map<String, BigDecimal> resultMap = expenseService.sortExpenses(expenses);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);

    }

    @GetMapping("/currencies")
    public ResponseEntity<?> getCurrencies() {
        return new ResponseEntity<>(currencyService.getCurrencies(), HttpStatus.OK);
    }
    @GetMapping("/barchartData")
    public ResponseEntity<?>getBarchartData(@RequestParam int userId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {  List<List<Expense>> barChartList= expenseService.getExpensesByBeginDateAndEndDateSortedBy(startDate,endDate,userId);
        return new ResponseEntity<>(barChartList,HttpStatus.OK);
    }

}


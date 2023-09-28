package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.CurrencyService;
import com.endava.expensesmanager.service.ExpenseService;
import com.endava.expensesmanager.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/total")
public class TotalController {
    private ExpenseService expenseService;
    @Autowired
    public void setExpenseService(ExpenseService expenseService)
    {
        this.expenseService=expenseService;
    }
    private CurrencyService currencyService;
@Autowired
public void setCurrencyService(CurrencyService currencyService)
{
    this.currencyService=currencyService;
}

    @Autowired
    CategoryServiceImpl categoryService;
    @GetMapping
    public ResponseEntity<?> getTotalSum(@RequestParam int userId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate,@RequestParam String code)
    {
        List<Expense> expenses=currencyService.changeCurrencyTo(code,expenseService.getExpensesByDates(startDate,endDate,userId));
        BigDecimal sum=BigDecimal.ZERO;
        for(int i=0;i<expenses.size();i++)
        {
            sum=sum.add(expenses.get(i).getAmount());
        }
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getTotalSumCategory(@RequestParam int userId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate,@RequestParam String code)
    {
        if(endDate.isAfter(LocalDate.now()))
            return new ResponseEntity<>("The end date is invalid",HttpStatus.BAD_REQUEST);

        List<Expense> expenses=currencyService.changeCurrencyTo(code,expenseService.getExpensesByDates(startDate,endDate,userId));
        List<Category> categories=categoryService.getAllCategories();
        Map<String, BigDecimal> resultMap = expenseService.sortExpenses(expenses);

          return new ResponseEntity<>(resultMap,HttpStatus.OK);

    }
    @GetMapping("/currencies")
   public ResponseEntity<?> getCurrencies()
   {
       return new ResponseEntity<>(currencyService.getCurrencies(),HttpStatus.OK);
   }


}



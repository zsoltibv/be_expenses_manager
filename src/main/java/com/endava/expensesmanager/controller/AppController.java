package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.Category;
import com.endava.expensesmanager.model.Expense;
import com.endava.expensesmanager.service.impl.CategoryServiceImpl;
import com.endava.expensesmanager.service.impl.ExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AppController {
    @Autowired
    ExpenseServiceImpl expenseService;
    @Autowired
    CategoryServiceImpl categoryService;
    @GetMapping("/total")
    public ResponseEntity<?> getTotalSum(@RequestParam int userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate)
    {
        List<Expense> expenses=expenseService.getExpensesByBeginDateAndEndDate(startDate,endDate,userId);
        BigDecimal sum=BigDecimal.ZERO;
        for(int i=0;i<=expenses.size();i++)
        {
            sum=sum.add(expenses.get(i).getAmount());
        }
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }
    @GetMapping("/totalCategory")
    public ResponseEntity<?> getTotalSumCategory(@RequestParam int userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate)
    { if(endDate.isAfter(LocalDateTime.now()))
        return new ResponseEntity<>("The end date is invalid",HttpStatus.BAD_REQUEST);
        List<Expense> expenses=expenseService.getExpensesByBeginDateAndEndDate(startDate,endDate,userId);
      List<Category> categories=categoryService.getAllCategories();
        Map<Integer, BigDecimal> resultMap = expenses.stream()
                .filter(expense -> categories.stream()
                        .anyMatch(category -> category.getCategoryId() == expense.getCategory().getCategoryId()))
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getCategoryId(),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
      return new ResponseEntity<>(resultMap,HttpStatus.OK);

    }

}


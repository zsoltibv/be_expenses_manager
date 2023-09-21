package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.Category;
import com.endava.expensesmanager.model.Expense;
import com.endava.expensesmanager.service.impl.CategoryServiceImpl;
import com.endava.expensesmanager.service.impl.ExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
public class AppController {
    @Autowired
    ExpenseServiceImpl expenseService;
    @Autowired
    CategoryServiceImpl categoryService;
    @GetMapping("/total")
    public BigDecimal getTotalSum(@RequestParam int userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate)
    {
        List<Expense> expenses=expenseService.getExpensesByBeginDateAndEndDate(startDate,endDate,userId);
        BigDecimal sum=BigDecimal.ZERO;
        for(int i=0;i<=expenses.size();i++)
        {
            sum=sum.add(expenses.get(i).getAmount());
        }
        return sum;
    }
    @GetMapping("/totalCategory")
    public HashMap<Integer,BigDecimal> getTotalSumCategory(@RequestParam int userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate)
    { List<Expense> expenses=expenseService.getExpensesByBeginDateAndEndDate(startDate,endDate,userId);
      List<Category> categories=categoryService.getAllCategories();
      HashMap<Integer,BigDecimal> map=new HashMap<>();

      for(int i=0;i<categories.size();i++)
       for(int j=0;j<expenses.size();j++)
      {   if(expenses.get(j).getCategory().getCategoryId()==categories.get(i).getCategoryId())
          { if(map.containsKey(categories.get(i).getCategoryId()))
          {
              map.put(categories.get(i).getCategoryId(),map.get(categories.get(i).getCategoryId()).add(expenses.get(j).getAmount()));
          }
          else
              map.put(categories.get(i).getCategoryId(),expenses.get(j).getAmount());
             break;
          }

      }
      return map;
        
    }

}

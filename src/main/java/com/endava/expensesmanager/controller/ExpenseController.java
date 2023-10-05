package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expense")
@Validated
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping()
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody @Valid ExpenseDto expenseDto){
        expenseService.addExpense(expenseDto);
        return new ResponseEntity<>(expenseDto, HttpStatus.CREATED);
    }

    @GetMapping("byUser/{userId}")
    public ResponseEntity<List<ExpenseDto>> getExpensesByUserId(@PathVariable Integer userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate){

        List<ExpenseDto> expenses = expenseService.getExpensesByUserId(userId, startDate, endDate);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExpenseById(@RequestParam Integer expenseId)
    {
        return expenseService.deleteExpenseById(expenseId);
    }
}

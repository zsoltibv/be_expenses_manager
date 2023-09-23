package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}

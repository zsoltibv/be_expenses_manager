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


    public ExpenseController(ExpenseService expenseService) {

        this.expenseService = expenseService;

    }

    @PostMapping()
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody @Valid ExpenseDto expenseDto) {

        expenseService.addExpense(expenseDto);
        return new ResponseEntity<>(expenseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDto> editExpense(@PathVariable Integer expenseId,
                                                  @RequestBody @Valid ExpenseDto expenseDto) {
        expenseService.editExpense(expenseId, expenseDto);
        return new ResponseEntity<>(expenseDto, HttpStatus.OK);
    }

    @GetMapping("byUser/{userId}")
    public ResponseEntity<List<ExpenseDto>> getExpensesByUserId(@PathVariable Integer userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {

        List<ExpenseDto> expenses = expenseService.getExpensesByUserId(userId, startDate, endDate);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }


    @PostMapping("/seed")
    public ResponseEntity<String> seedExpenses(@RequestParam(required = false) Integer nrOfExpenses, @RequestParam(required = false) Integer nrOfDays) {
        expenseService.seedExpenses(nrOfExpenses, nrOfDays);
        return new ResponseEntity<>("Expenses added successfully!", HttpStatus.CREATED);
    }

}


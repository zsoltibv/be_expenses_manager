package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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
    public ResponseEntity<ExpenseDto> addExpense(@RequestPart("expenseDto") @Valid ExpenseDto expenseDto,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        expenseService.addExpense(expenseDto, file);
        return new ResponseEntity<>(expenseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{expenseId}")

    public ResponseEntity<ExpenseDto> editExpense(@PathVariable Integer expenseId, @RequestBody @Valid ExpenseDto expenseDto) {

        expenseService.editExpense(expenseId, expenseDto);
        return new ResponseEntity<>(expenseDto, HttpStatus.OK);
    }

    @GetMapping("byUser/{userId}")
    public ResponseEntity<List<ExpenseDto>> getExpensesByUserId(@PathVariable Integer userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        List<ExpenseDto> expenses = expenseService.getExpensesByUserId(userId, startDate, endDate);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PostMapping("/extractAndSaveExpensesFromPdf/{userId}")
    public ResponseEntity<List<ExpenseDto>> extractAndSaveExpensesFromPdf(@PathVariable Integer userId, @RequestPart(value = "file") MultipartFile pdfFile) throws IOException {

        List<ExpenseDto> expenses = expenseService.extractAndSaveExpensesFromPdf(userId, pdfFile);
        return new ResponseEntity<>(expenses, HttpStatus.CREATED);

    }

    @PostMapping("/seed")
    public ResponseEntity<String> seedExpenses(@RequestParam(required = false) Integer nrOfExpenses, @RequestParam(required = false) Integer nrOfDays) {
        expenseService.seedExpenses(nrOfExpenses, nrOfDays);
        return new ResponseEntity<>("Expenses added successfully!", HttpStatus.CREATED);
    }


    @DeleteMapping("{expenseId}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Integer expenseId) {
        expenseService.deleteExpenseById(expenseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}


package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.service.DocumentService;
import com.endava.expensesmanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final DocumentService documentService;

    public ExpenseController(ExpenseService expenseService, DocumentService documentService) {
        this.expenseService = expenseService;
        this.documentService = documentService;
    }

    @PostMapping()
    public ResponseEntity<ExpenseDto> addExpense(@RequestPart("json") @Valid ExpenseDto expenseDto,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ExpenseDto expenseDtoWithFile = documentService.processFileUpload(file, expenseDto);
        expenseService.addExpense(expenseDtoWithFile);
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
}

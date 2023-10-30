package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ExpenseService {

    void editExpense(ExpenseDto expenseDto, MultipartFile file) throws IOException;

    List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);

    List<ExpenseDto> getExpensesByBeginDateAndEndDate(LocalDateTime beginDate, LocalDateTime endDate, Integer userId);

    void seedExpenses(Integer nrOfExpenses, Integer nrOfDays);

    void addExpense(ExpenseDto expenseDto, MultipartFile file) throws IOException;

    void deleteExpenseById(Integer expenseId);

    Map<String, BigDecimal> sortExpenses(List<ExpenseDto> expenses);

    List<ExpenseDto> extractAndSaveExpensesFromPdf(Integer userId, MultipartFile pdfFile) throws IOException;
}

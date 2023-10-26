package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ExpenseService {

    void addExpense(ExpenseDto expenseDto);

    void editExpense(Integer expenseId, ExpenseDto expenseDto);

    List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);

    List<ExpenseDto> getExpensesByBeginDateAndEndDate(LocalDateTime beginDate, LocalDateTime endDate, Integer userId);

    Map<String, BigDecimal> sortExpenses(List<ExpenseDto> expenses);

    void seedExpenses(Integer nrOfExpenses, Integer nrOfDays);
  
    void extractExpensesFromPdf(Integer userId, MultipartFile pdfFile) throws IOException;

}

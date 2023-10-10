package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public interface ExpenseService {
        void addExpense(ExpenseDto expenseDto, MultipartFile file) throws IOException;
        void editExpense(Integer expenseId, ExpenseDto expenseDto);
        List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);
}

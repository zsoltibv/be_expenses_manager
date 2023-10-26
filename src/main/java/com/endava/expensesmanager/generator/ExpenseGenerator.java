package com.endava.expensesmanager.generator;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDateTime;

public class ExpenseGenerator {
    private static final Faker faker = new Faker();

    public static Expense generateFakeExpense(int nrOfDays, List<Category> categories, List<Currency> currencies) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setDescription(generateFakeDescription());
        expenseDto.setAmount(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0.01, 1000)));
        expenseDto.setExpenseDate(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, nrOfDays)));
        expenseDto.setUserId(1);
        expenseDto.setCategory(categories.get(ThreadLocalRandom.current().nextInt(categories.size())));
        expenseDto.setCurrency(currencies.get(ThreadLocalRandom.current().nextInt(currencies.size())));
        return ExpenseMapper.toExpense(expenseDto);
    }

    private static String generateFakeDescription() {
        String description = faker.lorem().sentence();
        if (description.length() > 45) {
            description = description.substring(0, 45);
        }
        return description;
    }
}
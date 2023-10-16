package com.endava.expensesmanager.seeder;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDateTime;

public class ExpenseSeeder {
    private static final Faker faker = new Faker();

    public static void seedData(ExpenseRepository expenseRepository) {
        if (expenseRepository.count() > 0) {
            return;
        }
        for (int i = 0; i < 200; i++) {
            ExpenseDto expenseDto = generateFakeExpenseDto();
            expenseRepository.save(ExpenseMapper.toExpense(expenseDto));
        }
    }

    private static ExpenseDto generateFakeExpenseDto() {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setExpenseId(ThreadLocalRandom.current().nextInt(1, 1000));
        expenseDto.setDescription(generateFakeDescription());
        expenseDto.setAmount(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0.01, 1000)));
        expenseDto.setExpenseDate(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 90)));
        expenseDto.setUserId(1);
        expenseDto.setCategoryId(ThreadLocalRandom.current().nextInt(1, 7));
        expenseDto.setCurrencyId(ThreadLocalRandom.current().nextInt(1, 4));
        return expenseDto;
    }

    private static String generateFakeDescription() {
        String description = faker.lorem().sentence();
        if (description.length() > 45) {
            description = description.substring(0, 45);
        }
        return description;
    }
}
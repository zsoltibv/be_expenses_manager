package com.endava.expensesmanager.seeder;

import com.endava.expensesmanager.repository.ExpenseRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    private final ExpenseRepository expenseRepository;

    public DatabaseSeeder(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        ExpenseSeeder.seedData(expenseRepository);
    }
}
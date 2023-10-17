package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}

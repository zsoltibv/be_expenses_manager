package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}

package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("SELECT e FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate AND e.user.userId = :userId")
    List<Expense> findExpensesBetweenDatesForUser(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") int userId
    );

    @Query("SELECT e FROM Expense e WHERE e.user.userId = :userId")
    List<Expense> findByUserId(
            @Param("userId") int userId
    );
}

package com.endava.expensesmanager.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="expense_id")
    private int expenseId;
    @Column(name = "description")
    private String description;
    @Column(name = "amount",precision = 10,scale = 2)
    private BigDecimal amount;
    @Column(name = "expense_date")
    private LocalDateTime expenseDate;
    
}

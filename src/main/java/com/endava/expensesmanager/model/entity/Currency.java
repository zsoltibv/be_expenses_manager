package com.endava.expensesmanager.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private int currencyId;
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "currency_id" )
    private List<Expense> expenses;
}

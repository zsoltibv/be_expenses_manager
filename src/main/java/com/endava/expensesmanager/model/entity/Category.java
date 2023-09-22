package com.endava.expensesmanager.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "category")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;
    @Column(name = "description")
    private String description;
    @Column(name = "color")
    private String color;
    @OneToMany(mappedBy = "category_id" )
    private List<Expense> expenses;

    public int getCategoryId() {
        return categoryId;
    }
}

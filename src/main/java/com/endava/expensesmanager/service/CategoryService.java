package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
}

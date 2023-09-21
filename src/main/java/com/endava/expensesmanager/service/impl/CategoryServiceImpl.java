package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.repository.CategoryRepository;
import com.endava.expensesmanager.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

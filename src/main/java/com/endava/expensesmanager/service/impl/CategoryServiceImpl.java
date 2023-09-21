package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.Category;
import com.endava.expensesmanager.repository.CategoryRepository;
import com.endava.expensesmanager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

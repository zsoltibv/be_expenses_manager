package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

}

package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}

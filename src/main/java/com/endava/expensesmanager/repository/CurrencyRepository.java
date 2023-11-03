package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    @Query("SELECT c FROM Currency c WHERE c.code = :code")
    Currency findByCode(@Param("code") String code);

}



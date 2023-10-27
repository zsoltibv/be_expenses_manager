package com.endava.expensesmanager.repository;

import com.endava.expensesmanager.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}

package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.entity.Expense;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface BankStatementParser {
    List<Expense> parseBankStatement(InputStream inputStream) throws IOException;

}

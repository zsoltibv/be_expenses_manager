package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.BankStatementParser;
import com.endava.expensesmanager.transformer.DataTransformer;
import com.endava.expensesmanager.transformer.impl.DateDataTransformer;
import com.endava.expensesmanager.transformer.impl.DecimalDataTransformer;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BankStatementParserBRD implements BankStatementParser {
    final String dateFormat = "dd.MM.yyyy";
    final String decimalFormat = "#.##";
    @Override
    public List<Expense> parseBankStatement(InputStream inputStream) throws IOException {

        List<Expense> expensesList = new ArrayList<>();

        try (PDDocument document = PDDocument.load(inputStream)) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            while (pi.hasNext()) {
                Page page = pi.next();
                List<Table> tables = sea.extract(page);
                processTables(tables, expensesList);
            }
        }
        return expensesList;
    }

    private void processTables(List<Table> tables, List<Expense> expensesList) {
        
        for (int i = 0; i < tables.size(); i++) {
            List<List<RectangularTextContainer>> rows = tables.get(i).getRows();
            for (List<RectangularTextContainer> cells : rows) {
                Expense expense = createExpenseFromCells(cells);
                if (expense.getExpenseDate() != null) {
                    expensesList.add(expense);
                }
            }
        }
    }

    private Expense createExpenseFromCells(List<RectangularTextContainer> cells) {
        Expense expense = new Expense();
        for (int j = 0; j < cells.size(); j++) {
            RectangularTextContainer content = cells.get(j);
            String text = content.getText().replace("\r", " ").trim();
            switch (j) {
                case 1 -> {
                    DataTransformer<LocalDate> dateTransformer = new DateDataTransformer(dateFormat);
                    if (dateTransformer.isValidData(text)) {
                        LocalDate date = dateTransformer.getData(text);
                        expense.setExpenseDate(date.atStartOfDay());
                    }
                }
                case 3 -> expense.setDescription(text);
                case 5 -> {
                    DataTransformer<BigDecimal> decimalTransformer = new DecimalDataTransformer(decimalFormat);
                    BigDecimal amount = decimalTransformer.getData(text);
                    expense.setAmount(amount);
                }
                default -> {
                }
            }
        }
        return expense;
    }
}
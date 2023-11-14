package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
import com.endava.expensesmanager.generator.ExpenseGenerator;
import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.*;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.CategoryRepository;
import com.endava.expensesmanager.repository.CurrencyRepository;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.repository.UserRepository;
import com.endava.expensesmanager.service.DocumentService;

import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;
    private final DocumentService documentService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, CurrencyRepository currencyRepository, DocumentService documentService) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
        this.documentService = documentService;
    }

    @Override
    public void addExpense(ExpenseDto expenseDto, MultipartFile file) throws IOException {
        if (!userRepository.existsById(expenseDto.getUserId())) {
            throw new UserNotFoundException(expenseDto.getUserId());
        }

        if (file != null) {
            Integer documentId = documentService.addDocumentAndGetId(file);
            expenseDto.setDocumentId(documentId);
        }

        expenseRepository.save(ExpenseMapper.toExpense(expenseDto));
    }

    @Override
    public void editExpense(Integer expenseId, ExpenseDto expenseDto, MultipartFile file) throws IOException {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));

        Document existingDocument = existingExpense.getDocument().orElse(null);

        User user = userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(expenseDto.getUserId()));

        Integer documentId = null;
        if (file != null) {
            documentId = documentService.editDocumentAndGetId(expenseDto, file);
            expenseDto.setDocumentId(documentId);
        }

        Optional<Document> document = documentId != null
                ? Optional.ofNullable(documentService.getDocumentById(documentId))
                : Optional.empty();

        Expense updatedExpense = ExpenseMapper.toUpdatedExpense(existingExpense, expenseDto, user, document.orElse(null));

        expenseRepository.save(updatedExpense);
        if (existingDocument != null) {
            documentService.deleteDocument(existingDocument);
        }
    }

    @Override
    public List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDateTime startDate, LocalDateTime endDate) {

        Stream<Expense> expensesStream = expenseRepository.findAll().stream()
                .filter(expense -> expense.getUser().getUserId().equals(userId));

        if (startDate != null) {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().isAfter(startDate)
                    || expense.getExpenseDate().isEqual(startDate));
        }

        if (endDate != null) {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().isBefore(endDate)
                    || expense.getExpenseDate().isEqual(endDate));
        } else {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().isBefore(LocalDateTime.now())
                    || expense.getExpenseDate().isEqual(LocalDateTime.now()));
        }

        return expensesStream
                .map(ExpenseMapper::toDto)
                .toList();

    }


    public List<ExpenseDto> getExpensesByBeginDateAndEndDate(LocalDateTime beginDate, LocalDateTime endDate, Integer userId) {
        List<Expense> expenses = expenseRepository.findExpensesBetweenDatesForUser(beginDate, endDate, userId);
        List<ExpenseDto> expenseDto = new ArrayList<>();
        for (Expense expense : expenses) {
            expenseDto.add(ExpenseMapper.toDto(expense));
        }
        return expenseDto;

    }


    public Map<String, BigDecimal> sortExpenses(List<ExpenseDto> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> categoryRepository.findById(expense.getCategory().getCategoryId()).get().getDescription(),
                        Collectors.mapping(ExpenseDto::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    public List<List<ExpenseDto>> getExpensesByBeginDateAndEndDateSortedBy(LocalDateTime beginDate, LocalDateTime endDate, Integer userId) {
        List<List<ExpenseDto>> expenseList = new ArrayList<>();
        LocalDate beginDateLocalDate = beginDate.toLocalDate();
        LocalDate endDateLocalDate = endDate.toLocalDate();
        if (beginDateLocalDate.compareTo(endDateLocalDate) == 0) {
            while (beginDate.isBefore(endDate)) {
                expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDate, beginDate.plusHours(1).minusSeconds(1), userId));
                beginDate = beginDate.plusHours(1);
            }

        } else if (beginDate.getYear() == endDate.getYear() && beginDate.getMonth() == endDate.getMonth()) {
            LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while (increment.compareTo(endDate.toLocalDate()) <= 0) {
                expenseList.add(this.getExpensesByBeginDateAndEndDate(increment.atStartOfDay(), increment.atStartOfDay().plusHours(24).minusSeconds(1), userId));
                increment = increment.plusDays(1);
            }

        } else {
            LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while (endDate.toLocalDate().compareTo(increment.with(TemporalAdjusters.firstDayOfMonth())) >= 0) {
                LocalDate beginDateMonth = increment.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endDateMonth = increment.with(TemporalAdjusters.lastDayOfMonth());
                if (beginDateMonth.isBefore(beginDate.toLocalDate()))
                    beginDateMonth = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
                if (endDateMonth.isAfter(endDate.toLocalDate()))
                    endDateMonth = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDateMonth.atStartOfDay(), endDateMonth.atStartOfDay().plusHours(24).minusSeconds(1), userId));
                increment = increment.plusMonths(1);
            }
        }
        return expenseList;

    }

    @Override
    public void seedExpenses(Integer nrOfExpenses, Integer nrOfDays) {
        expenseRepository.deleteAll();

        nrOfExpenses = Optional.ofNullable(nrOfExpenses)
                .filter(n -> n <= 2000)
                .orElse(200);

        nrOfDays = Optional.ofNullable(nrOfDays)
                .filter(n -> n <= 540)
                .orElse(90);

        List<Expense> expensesList = new ArrayList<>();
        for (int i = 0; i < nrOfExpenses; i++) {
            expensesList.add(ExpenseGenerator.generateFakeExpense(
                    nrOfDays,
                    categoryRepository.findAll(),
                    currencyRepository.findAll()
            ));
        }

        expenseRepository.saveAll(expensesList);
    }

    @Override
    public void deleteExpenseById(Integer expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));

        expenseRepository.deleteById(expenseId);

        Optional<Document> optionalDocument = expense.getDocument();
        optionalDocument.ifPresent(documentService::deleteDocument);
    }

    @Override
    public List<ExpenseDto> extractAndSaveExpensesFromPdf(Integer userId, MultipartFile pdfFile) throws IOException {

        List<Expense> expensesList = new ArrayList<>();

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Category category = categoryRepository.findByDescription("Bank Statement");

        Currency currency = currencyRepository.findByCode("RON");

        try (InputStream pdfInputStream = pdfFile.getInputStream()) {
            expensesList = new BankStatementParserBRD().parseBankStatement(pdfInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        expensesList.stream()
                .forEach(expense -> {
                    expense.setUser(userRepository.findById(userId).get());
                    expense.setCategory(category);
                    expense.setCurrency(currency);
                    expenseRepository.save(expense);
                });

        return expensesList.stream()
                .map(ExpenseMapper::toDto)
                .toList();
    }
}
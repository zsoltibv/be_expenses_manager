package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
import com.endava.expensesmanager.generator.ExpenseGenerator;
import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.entity.User;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.CategoryRepository;
import com.endava.expensesmanager.repository.CurrencyRepository;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.repository.UserRepository;
import com.endava.expensesmanager.service.DocumentService;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        User user = userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(expenseDto.getUserId()));

        if (file != null) {
            Integer documentId = documentService.editDocumentAndGetId(expenseDto, file);
            expenseDto.setDocumentId(documentId);
        }

        Expense updatedExpense = ExpenseMapper.toUpdatedExpense(existingExpense, expenseDto, user, expenseDto.getCategory(), expenseDto.getCurrency());
        expenseRepository.save(updatedExpense);
    }

    @Override
    public List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate) {

        Stream<Expense> expensesStream = expenseRepository.findAll().stream()
                .filter(expense -> expense.getUser().getUserId().equals(userId));

        if (startDate != null) {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isAfter(startDate)
                    || expense.getExpenseDate().toLocalDate().isEqual(startDate));
        }

        if (endDate != null) {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isBefore(endDate)
                    || expense.getExpenseDate().toLocalDate().isEqual(endDate));
        } else {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isBefore(LocalDate.now())
                    || expense.getExpenseDate().toLocalDate().isEqual(LocalDate.now()));
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
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        if (expenseOptional.isPresent()) {
            expenseRepository.deleteById(expenseId);

            expenseOptional.ifPresent(expense -> {
                expense.getDocument().ifPresent(document -> documentService.deleteDocumentById(document.getDocumentId()));
            });
            return;
        }

        throw new ExpenseNotFoundException(expenseId);
    }

    @Override
    public List<ExpenseDto> extractAndSaveExpensesFromPdf(Integer userId, MultipartFile pdfFile) throws IOException {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Category category = categoryRepository.findByDescription("Bank Statement");

        Currency currency = currencyRepository.findByCode("RON");

        InputStream pdfInputStream = pdfFile.getInputStream();

        List<Expense> expensesList = new BankStatementParserBRD().parseBankStatement(pdfInputStream);

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
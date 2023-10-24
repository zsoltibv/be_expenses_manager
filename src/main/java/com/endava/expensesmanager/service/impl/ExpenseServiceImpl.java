package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.CategoryNotFoundException;
import com.endava.expensesmanager.exception.CurrencyNotFoundException;
import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
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
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.time.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, CurrencyRepository currencyRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
    }


    @Override
    public void addExpense(ExpenseDto expenseDto) {
        if (!userRepository.existsById(expenseDto.getUserId())) {
            throw new UserNotFoundException(expenseDto.getUserId());
        }

        if (!categoryRepository.existsById(expenseDto.getCategoryId())) {
            throw new CategoryNotFoundException(expenseDto.getCategoryId());
        }

        if (!currencyRepository.existsById(expenseDto.getCurrencyId())) {
            throw new CurrencyNotFoundException(expenseDto.getCurrencyId());
        }

        expenseRepository.save(ExpenseMapper.toExpense(expenseDto));
    }

    @Override
    public void editExpense(Integer expenseId, ExpenseDto expenseDto) {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));

        User user = userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(expenseDto.getUserId()));

        Category category = categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(expenseDto.getCategoryId()));

        Currency currency = currencyRepository.findById(expenseDto.getCurrencyId())
                .orElseThrow(() -> new CurrencyNotFoundException(expenseDto.getCurrencyId()));

        Expense updatedExpense = ExpenseMapper.toUpdatedExpense(existingExpense, expenseDto, user, category, currency);

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
                        expense -> categoryRepository.findById(expense.getCategoryId()).get().getDescription(),
                        Collectors.mapping(ExpenseDto::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
    public List<List<ExpenseDto>> getExpensesByBeginDateAndEndDateSortedBy(LocalDateTime beginDate, LocalDateTime endDate, Integer userId)
    { WeekFields weekFields = WeekFields.of(Locale.getDefault());
       List< List<ExpenseDto>> expenseList=new ArrayList<>();
       LocalDate beginDateLocalDate=beginDate.toLocalDate();
       LocalDate endDateLocalDate=endDate.toLocalDate();
        if (beginDateLocalDate.compareTo(endDateLocalDate) == 0) {
            while(beginDate.isBefore(endDate))
            { expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDate,beginDate.plusHours(1).minusSeconds(1),userId));
                beginDate=beginDate.plusHours(1);
            }

        }

        else if(beginDate.get(weekFields.weekOfWeekBasedYear())==endDate.get(weekFields.weekOfWeekBasedYear()))
        {
            LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while(increment.compareTo(endDate.toLocalDate())<=0)
            {expenseList.add(this.getExpensesByBeginDateAndEndDate(increment.atStartOfDay(),increment.atStartOfDay().plusHours(24).minusSeconds(1),userId));
               increment=increment.plusDays(1);
            }

        }
       else if(beginDate.getYear() == endDate.getYear() && beginDate.getMonth() == endDate.getMonth())
        { LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while (endDate.toLocalDate().compareTo(increment.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))>=0) {
                LocalDate beginDateWeek = increment.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endDateWeek = increment.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                if (beginDateWeek.isBefore(beginDate.toLocalDate()))
                    beginDateWeek = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
                if (endDateWeek.isAfter(endDate.toLocalDate()))
                    endDateWeek = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDateWeek.atStartOfDay(),endDateWeek.atStartOfDay().plusHours(24).minusSeconds(1),userId));
                increment=increment.plusWeeks(1);
            }
        }
       else{
            LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while (endDate.toLocalDate().compareTo(increment.with(TemporalAdjusters.firstDayOfMonth()))>=0) {
                LocalDate beginDateMonth = increment.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endDateMonth = increment.with(TemporalAdjusters.lastDayOfMonth());
                if (beginDateMonth.isBefore(beginDate.toLocalDate()))
                    beginDateMonth = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
                if (endDateMonth.isAfter(endDate.toLocalDate()))
                    endDateMonth = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDateMonth.atStartOfDay(),endDateMonth.atStartOfDay().plusHours(24).minusSeconds(1),userId));
                increment=increment.plusMonths(1);
            }
        }
        return expenseList;

    }
}

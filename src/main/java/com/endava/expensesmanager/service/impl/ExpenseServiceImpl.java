package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.dto.ExchangeRatesDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }


    @Override
    public Expense addExpense(ExpenseDto expense) {
        return expenseRepository.save(ExpenseMapper.toExpense(expense));
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

    public List<Expense> getExpensesByBeginDateAndEndDate(LocalDate beginDate, LocalDate endDate, Integer userId) {
        return expenseRepository.findExpensesBetweenDatesForUser(beginDate.atStartOfDay(), endDate.atStartOfDay().plusHours(24).minusSeconds(1), userId);
    }

    @Override
    public List<Expense> getExpensesByDates(LocalDate beginDate, LocalDate endDate, Integer userId) {
        return this.getExpensesByBeginDateAndEndDate(beginDate, endDate, userId);
    }

    public Map<String, BigDecimal> sortExpenses(List<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getDescription(),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
    public List<List<Expense>> getExpensesByBeginDateAndEndDateSortedBy(LocalDate beginDate, LocalDate endDate, Integer userId)
    { WeekFields weekFields = WeekFields.of(Locale.getDefault());
       List< List<Expense>> expenseList=new ArrayList<>();
        if (beginDate.compareTo(endDate) == 0) {
            List<Expense> dayExpenses = this.getExpensesByBeginDateAndEndDate(beginDate, endDate, userId);
            System.out.println(dayExpenses.size());
            LocalDateTime startOfDay = beginDate.atStartOfDay();
            LocalDateTime endOfDay = beginDate.plusDays(1).atStartOfDay();

            while (startOfDay.isBefore(endOfDay)) {

                List<Expense> expenseByHour = new ArrayList<>();
                for (Expense expense : dayExpenses) {
                    if (expense.getExpenseDate().compareTo(startOfDay)>=0 && expense.getExpenseDate().isBefore(startOfDay.plusHours(1))) {
                        expenseByHour.add(expense);
                    }
                }
                expenseList.add(expenseByHour);
                startOfDay = startOfDay.plusHours(1);
            }
            return expenseList;
        }

        else if(beginDate.get(weekFields.weekOfWeekBasedYear())==endDate.get(weekFields.weekOfWeekBasedYear()))
        {  LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while(increment.compareTo(endDate)<=0)
            {expenseList.add(this.getExpensesByBeginDateAndEndDate(increment,increment,userId));
               increment=increment.plusDays(1);
            }

        }
       else if(beginDate.getYear() == endDate.getYear() && beginDate.getMonth() == endDate.getMonth())
        { LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while (!endDate.isBefore(increment.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))) {
                LocalDate beginDateWeek = increment.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endDateWeek = increment.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                if (beginDateWeek.isBefore(beginDate))
                    beginDateWeek = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
                if (endDateWeek.isAfter(endDate))
                    endDateWeek = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDateWeek,endDateWeek,userId));
                increment=increment.plusWeeks(1);
            }
        }
       else{
            LocalDate increment = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
            while (endDate.compareTo(increment.with(TemporalAdjusters.firstDayOfMonth()))>=0) {
                LocalDate beginDateMonth = increment.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endDateMonth = increment.with(TemporalAdjusters.lastDayOfMonth());
                if (beginDateMonth.isBefore(beginDate))
                    beginDateMonth = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
                if (endDateMonth.isAfter(endDate))
                    endDateMonth = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                expenseList.add(this.getExpensesByBeginDateAndEndDate(beginDateMonth,endDateMonth,userId));
                increment=increment.plusMonths(1);
            }
        }
        return expenseList;

    }
}

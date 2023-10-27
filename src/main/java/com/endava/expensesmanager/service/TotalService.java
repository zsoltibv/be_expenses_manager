package com.endava.expensesmanager.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TotalService {

     BigDecimal totalExpenseSum(int userId, LocalDateTime startDate, LocalDateTime endDate, String code);
     Map<String,BigDecimal> totalExpenseCategory(int userId, LocalDateTime startDate, LocalDateTime endDate, String code);

    List<Map<String,BigDecimal>> barchartData(int userId, LocalDateTime startDate, LocalDateTime endDate, String code);

}

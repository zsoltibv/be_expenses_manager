package com.endava.expensesmanager.model.dto;

import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Currency;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class ExpenseDto {
    private Integer expenseId;
    @NotBlank(message = "Description is required")
    @Size(max = 45, message = "Description must be less than or equal to 45 characters")
    private String description;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than or equal to 0.01")
    @Digits(integer = 10, fraction = 2, message = "Amount can have at most 2 decimal places and 10 digits")
    private BigDecimal amount;
    @NotNull(message = "Expense date is required")
    @PastOrPresent(message = "Expense date can not exceed today's date")
    private LocalDateTime expenseDate;
    @NotNull(message = "User id is required")
    private Integer userId;
    private Integer documentId;
    @NotNull(message = "Category is required")
    private Category category;
    @NotNull(message = "Currency is required")
    private Currency currency;
}

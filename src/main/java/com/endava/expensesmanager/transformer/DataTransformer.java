package com.endava.expensesmanager.transformer;

public interface DataTransformer<T> {
    boolean isValidData(String dateStr);
    T getData(String dateStr);
}

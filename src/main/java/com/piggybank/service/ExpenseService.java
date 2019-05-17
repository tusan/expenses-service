package com.piggybank.service;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    void save(ExpenseDto expense);

    List<ExpenseDto> getAllExpenses(LocalDate dateStart, LocalDate dateEnd);
}

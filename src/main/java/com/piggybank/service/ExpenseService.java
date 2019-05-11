package com.piggybank.service;

import com.piggybank.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    void save(Expense expense);

    List<Expense> getAllExpenses(LocalDate dateStart, LocalDate dateEnd);
}

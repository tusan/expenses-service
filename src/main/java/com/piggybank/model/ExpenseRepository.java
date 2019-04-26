package com.piggybank.model;

import java.util.List;

public interface ExpenseRepository {
    List<Expense> getAllExpenses();
    void save(Expense expense);
}

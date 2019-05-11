package com.piggybank.service;

import com.google.common.base.Preconditions;
import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    ExpenseServiceImpl(final ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void save(final Expense expense) {
        expenseRepository
                .save(expense);
    }

    @Override
    public List<Expense> getAllExpenses(final LocalDate dateStart, final LocalDate dateEnd) {
        Preconditions.checkArgument(dateStart != null);
        Preconditions.checkArgument(dateEnd != null);

        return expenseRepository
                .getAllExpenses()
                .stream()
                .filter(exp -> exp.date().isAfter(dateStart.minus(1, DAYS))
                        && exp.date().isBefore(dateEnd.plus(1, DAYS)))
                .collect(Collectors.toList());
    }
}

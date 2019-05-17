package com.piggybank.service;

import com.google.common.base.Preconditions;
import com.piggybank.model.ExpenseRepository;
import com.piggybank.model.ExpenseConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.piggybank.model.ExpenseConverter.dtoToEntity;
import static java.time.temporal.ChronoUnit.DAYS;

class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    ExpenseServiceImpl(final ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void save(final ExpenseDto expense) {
        expenseRepository
                .save(dtoToEntity(expense));
    }

    @Override
    public List<ExpenseDto> getAllExpenses(final LocalDate dateStart, final LocalDate dateEnd) {
        Preconditions.checkArgument(dateStart != null);
        Preconditions.checkArgument(dateEnd != null);

        return expenseRepository
                .getAllExpenses()
                .stream()
                .filter(exp -> exp.date().isAfter(dateStart.minus(1, DAYS))
                        && exp.date().isBefore(dateEnd.plus(1, DAYS)))
                .map(ExpenseConverter::entityToDto)
                .collect(Collectors.toList());
    }
}

package com.piggybank.service;

import com.piggybank.model.ExpenseRepository;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ExpenseServiceModule {
    @Provides
    @Singleton
    static ExpenseService provideExpenseService(ExpenseRepository expenseRepository) {
        return new ExpenseServiceImpl(expenseRepository);
    }
}
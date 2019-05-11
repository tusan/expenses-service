package com.piggybank.model;

import com.piggybank.context.JdbcConnectionProvider;

public class JdbcExpenseRepositoryFactory implements ExpenseRepositoryFactory {
    private final static Object LOCK = new Object();
    private final JdbcConnectionProvider jdbcConnectionProvider;

    private static ExpenseRepository INSTANCE;

    public JdbcExpenseRepositoryFactory(JdbcConnectionProvider jdbcConnectionProvider) {
        this.jdbcConnectionProvider = jdbcConnectionProvider;
    }

    @Override
    public ExpenseRepository getRepository() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new JdbcExpenseRepository(jdbcConnectionProvider.forCurrentConfigs());
            }
        }
        return INSTANCE;
    }
}

package com.piggybank.model;

import com.piggybank.util.ExternalConfReader;
import com.piggybank.util.JdbcConnectionProvider;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class JdbcExpenseRepositoryModule {
    @Provides
    @Singleton
    static ExpenseRepository provideJdbcRepository(JdbcConnectionProvider jdbcConnectionProvider) {
        return new JdbcExpenseRepository(jdbcConnectionProvider);
    }

    @Provides
    @Singleton
    static JdbcConnectionProvider provideJdbcConnectionProvider(ExternalConfReader externalConfReader) {
        return new JdbcConnectionProvider(externalConfReader);
    }
}

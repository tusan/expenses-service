package com.piggybank.model;

import com.piggybank.util.ExternalConfReader;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import javax.sql.DataSource;

@Module
public class JdbcExpenseRepositoryModule {
    public final static String DATABASE_URL = "DATABASE_URL";
    public final static String DATABASE_USER = "DATABASE_USER";
    public final static String DATABASE_PASSWORD = "DATABASE_PASSWORD";

    @Provides
    @Singleton
    static ExpenseRepository provideJdbcRepository(DataSource dataSource) {
        return new JdbcExpenseRepository(dataSource);
    }

    @Provides
    @Singleton
    public static DataSource provideDataSource(ExternalConfReader externalConfReader) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(externalConfReader.get(DATABASE_URL).orElseThrow(IllegalArgumentException::new));
        dataSource.setUsername(externalConfReader.get(DATABASE_USER).orElseThrow(IllegalArgumentException::new));
        dataSource.setPassword(externalConfReader.get(DATABASE_PASSWORD).orElseThrow(IllegalArgumentException::new));

        return dataSource;
    }
}

package com.piggybank;

import com.piggybank.model.JdbcExpenseRepositoryModule;
import com.piggybank.server.EmbeddedServerModule;
import com.piggybank.service.ExpenseServiceModule;
import com.piggybank.util.ExternalConfReader;
import dagger.BindsInstance;
import dagger.Component;
import io.undertow.Undertow;

import javax.inject.Singleton;

public class ExpensesApp {
    public static void main(String[] args) {
        DaggerExpensesApp_ExpenseAppComponent.builder()
                .externalConfReader(new ExternalConfReader.EnvExternalConfReader())
                .build()
                .injectDependencies()
                .start();

    }

    @Singleton
    @Component(modules = {EmbeddedServerModule.class, ExpenseServiceModule.class, JdbcExpenseRepositoryModule.class})
    public interface ExpenseAppComponent {
        Undertow injectDependencies();

        @Component.Builder
        interface Builder {
            @BindsInstance
            Builder externalConfReader(ExternalConfReader externalConfReader);

            ExpenseAppComponent build();
        }
    }
}
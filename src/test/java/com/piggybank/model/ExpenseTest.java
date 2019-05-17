package com.piggybank.model;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public class ExpenseTest {

    @Test
    public void shouldBuildAnExpenseWithAllMandatoryFields() {
        Expense.newBuilder()
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildAnExpenseWithoutOwner() {
        Expense.newBuilder()
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldBuildAnExpenseWithoutDate() {
        Expense.newBuilder()
                .owner("example@example.it")
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldBuildAnExpenseWithoutType() {
        Expense.newBuilder()
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .amount(24.5)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldBuildAnExpenseWithoutAmount() {
        Expense.newBuilder()
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .build();
    }
}

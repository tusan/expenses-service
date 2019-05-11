package com.piggybank.service;

import com.piggybank.model.Expense;
import com.piggybank.model.ExpenseRepository;
import com.piggybank.model.ExpenseType;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExpenseServiceImplTest {
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullDateStart() {
        new ExpenseServiceImpl(new MockExpenseRepository(null))
                .getAllExpenses(null, LocalDate.of(2019, Month.JUNE, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullDateEnd() {
        new ExpenseServiceImpl(new MockExpenseRepository(null))
                .getAllExpenses(LocalDate.of(2019, Month.JUNE, 1), null);
    }

    @Test
    public void shouldReturnAnEmptyListWhenNoDataIsReturnedFromRepository() {
        ExpenseServiceImpl sut = new ExpenseServiceImpl(new MockExpenseRepository(Collections.emptyList()));

        Assert.assertTrue(sut.getAllExpenses(LocalDate.of(2019, Month.JUNE, 1), LocalDate.of(2019, 6, 1)).isEmpty());
    }

    @Test
    public void shouldFilterByDateStart() {
        ExpenseServiceImpl sut = new ExpenseServiceImpl(new MockExpenseRepository(Arrays.asList(
                buildExpense(LocalDate.of(2019, Month.MAY, 2)),
                buildExpense(LocalDate.of(2019, Month.MAY, 3)),
                buildExpense(LocalDate.of(2019, Month.MAY, 4))
        )));

        assertEquals(
                sut.getAllExpenses(
                        LocalDate.of(2019, Month.MAY, 3),
                        LocalDate.of(2019, Month.MAY, 20)
                ),
                Arrays.asList(
                        buildExpense(LocalDate.of(2019, Month.MAY, 3)),
                        buildExpense(LocalDate.of(2019, Month.MAY, 4))
                )
        );
    }

    @Test
    public void shouldFilterByDateEnd() {
        ExpenseServiceImpl sut = new ExpenseServiceImpl(new MockExpenseRepository(Arrays.asList(
                buildExpense(LocalDate.of(2019, Month.MAY, 2)),
                buildExpense(LocalDate.of(2019, Month.MAY, 3)),
                buildExpense(LocalDate.of(2019, Month.MAY, 4))
        )));

        assertEquals(
                sut.getAllExpenses(
                        LocalDate.of(2019, Month.MAY, 1),
                        LocalDate.of(2019, Month.MAY, 3)
                ),
                Arrays.asList(
                        buildExpense(LocalDate.of(2019, Month.MAY, 2)),
                        buildExpense(LocalDate.of(2019, Month.MAY, 3))
                )
        );
    }

    @Test
    public void shouldFilterDateInRange() {
        ExpenseServiceImpl sut = new ExpenseServiceImpl(new MockExpenseRepository(Arrays.asList(
                buildExpense(LocalDate.of(2019, Month.MAY, 1)),
                buildExpense(LocalDate.of(2019, Month.MAY, 2)),
                buildExpense(LocalDate.of(2019, Month.MAY, 3)),
                buildExpense(LocalDate.of(2019, Month.MAY, 4)),
                buildExpense(LocalDate.of(2019, Month.MAY, 5))
        )));

        assertEquals(
                sut.getAllExpenses(
                        LocalDate.of(2019, Month.MAY, 2),
                        LocalDate.of(2019, Month.MAY, 4)
                ),
                Arrays.asList(
                        buildExpense(LocalDate.of(2019, Month.MAY, 2)),
                        buildExpense(LocalDate.of(2019, Month.MAY, 3)),
                        buildExpense(LocalDate.of(2019, Month.MAY, 4))
                )
        );
    }

    @Test
    public void shouldSaveAnExpenseOnTheRepository() {
        final MockExpenseRepository factoryMock = new MockExpenseRepository(null);

        ExpenseServiceImpl sut = new ExpenseServiceImpl(factoryMock);

        sut.save(buildExpense(LocalDate.of(2019, Month.MAY, 3)));

        assertEquals(buildExpense(LocalDate.of(2019, Month.MAY, 3)), factoryMock.saved);
    }

    private Expense buildExpense(LocalDate localDate) {
        return Expense.newBuilder()
                .owner("example@test.org")
                .date(localDate)
                .type(ExpenseType.MOTORBIKE)
                .description("test 2")
                .amount(5.4)
                .build();
    }

    private class MockExpenseRepository implements ExpenseRepository {
        private final List<Expense> mockedResult;
        private Expense saved;

        MockExpenseRepository(List<Expense> mockedResult) {
            this.mockedResult = mockedResult;
        }

        @Override
        public List<Expense> getAllExpenses() {
            return mockedResult;
        }

        @Override
        public void save(Expense expense) {
            saved = expense;
        }
    }
}
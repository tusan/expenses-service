package com.piggybank.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.piggybank.model.ResultSetConverter.toExpense;
import static com.piggybank.util.ExceptionUtils.wrapCheckedException;

class JdbcExpenseRepository implements ExpenseRepository {
    private static final String SELECT_ALL = "select id, owner, type, description, date, amount from expenses order by date desc limit 1000";
    private static final String INSERT = "insert into expenses (owner, type, description, date, amount) values('%s', '%s', '%s', '%s', '%s')";

    private final Connection connection;

    JdbcExpenseRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Expense> getAllExpenses() {
        return wrapCheckedException(() -> {
            ResultSet resultSet = connection
                    .createStatement()
                    .executeQuery(SELECT_ALL);

            ArrayList<Expense> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(toExpense(resultSet));
            }

            return result;
        });
    }

    @Override
    public void save(Expense expense) {
        wrapCheckedException(() ->
                connection.createStatement()
                        .executeUpdate(String.format(
                                INSERT,
                                expense.owner(),
                                expense.type(),
                                expense.description(),
                                expense.date(),
                                expense.amount()
                        )));
    }
}

package com.piggybank.model;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.piggybank.model.ExpenseConverter.resultSetToEntity;
import static com.piggybank.util.ExceptionUtils.wrapCheckedException;

class JdbcExpenseRepository implements ExpenseRepository {
    private static final String SELECT_ALL = "select id, owner, type, description, date, amount from expenses order by date desc limit 1000";
    private static final String INSERT = "insert into expenses (owner, type, description, date, amount) values('%s', '%s', '%s', '%s', '%s')";

    private final DataSource dataSource;

    JdbcExpenseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Expense> getAllExpenses() {
        return wrapCheckedException(() -> {
            ResultSet resultSet = dataSource
                    .getConnection()
                    .createStatement()
                    .executeQuery(SELECT_ALL);

            ArrayList<Expense> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(resultSetToEntity(resultSet));
            }

            return result;
        });
    }

    @Override
    public void save(Expense expense) {
        wrapCheckedException(() ->
                dataSource.getConnection()
                        .createStatement()
                        .executeUpdate(String.format(
                                INSERT,
                                expense.owner(),
                                expense.type(),
                                expense.description().orElse(null),
                                expense.date(),
                                expense.amount()
                        )));
    }
}

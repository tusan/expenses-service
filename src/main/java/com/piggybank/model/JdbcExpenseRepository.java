package com.piggybank.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcExpenseRepository implements ExpenseRepository {
    private static final String SELECT_ALL = "select id, owner, type, description, date, amount from expenses";
    private static final String INSERT = "insert into expenses (owner, type, description, date, amount) values('%s', '%s', '%s', '%s', '%s')";

    private final Connection connection;

    public JdbcExpenseRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Expense> getAllExpenses() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_ALL);

            ArrayList<Expense> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(convert(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Expense expense) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format(INSERT,
                    expense.owner(),
                    expense.type(),
                    expense.description(),
                    expense.date(),
                    expense.amount()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Expense convert(ResultSet resultSet) throws SQLException {
       return Expense.newBuilder()
                        .id(resultSet.getLong(1))
                        .owner(formatStringValue(resultSet.getString(2)))
                        .type(ExpenseType.valueOf(resultSet.getString(3)))
                        .description(formatStringValue(resultSet.getString(4)))
                        .date(resultSet.getDate(5).toLocalDate())
                        .amount(resultSet.getDouble(6))
                        .build();
    }

    private static String formatStringValue(String value) {
        if(value == null || value.equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }
}

package com.piggybank.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetConverter {
    public static Expense toExpense(final ResultSet resultSet) throws SQLException {
        return Expense.newBuilder()
                .id(resultSet.getLong(1))
                .owner(formatStringValue(resultSet.getString(2)))
                .type(ExpenseType.valueOf(resultSet.getString(3)))
                .description(formatStringValue(resultSet.getString(4)))
                .date(resultSet.getDate(5).toLocalDate())
                .amount(resultSet.getDouble(6))
                .build();
    }

    private static String formatStringValue(final String value) {
        if (value == null || value.equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }
}

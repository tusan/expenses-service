package com.piggybank.model;

import com.piggybank.service.ExpenseDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseConverter {
    public static Expense resultSetToEntity(final ResultSet resultSet) throws SQLException {
        return Expense.newBuilder()
                .id(resultSet.getLong(1))
                .owner(formatStringValue(resultSet.getString(2)))
                .type(ExpenseType.valueOf(resultSet.getString(3)))
                .description(formatStringValue(resultSet.getString(4)))
                .date(resultSet.getDate(5).toLocalDate())
                .amount(resultSet.getDouble(6))
                .build();
    }

    public static ExpenseDto entityToDto(Expense entity) {
        return ExpenseDto.newBuilder()
                .amount(entity.amount())
                .date(entity.date())
                .description(entity.description().orElse(null))
                .owner(entity.owner())
                .type(entity.type())
                .build();
    }

    public static Expense dtoToEntity(ExpenseDto dto) {
        return Expense.newBuilder()
                .amount(dto.amount())
                .date(dto.date())
                .description(dto.description().orElse(null))
                .owner(dto.owner())
                .type(dto.type())
                .build();
    }

    private static String formatStringValue(final String value) {
        if (value == null || value.equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }
}

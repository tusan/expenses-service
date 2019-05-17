package com.piggybank.model;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Optional;

@AutoValue
public abstract class Expense {
    public static Builder newBuilder() {
        return new AutoValue_Expense.Builder();
    }

    @Nullable
    public abstract Long id();

    public abstract String owner();

    public abstract ExpenseType type();

    public abstract Optional<String> description();

    public abstract LocalDate date();

    public abstract double amount();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long id);

        public abstract Builder owner(String owner);

        public abstract Builder type(ExpenseType type);

        public abstract Builder description(String description);

        public abstract Builder date(LocalDate date);

        public abstract Builder amount(double amount);

        public abstract Expense build();
    }
}

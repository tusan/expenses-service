package com.piggybank.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Objects;

@JsonDeserialize(builder = Expense.Builder.class)
public class Expense {
    private final Long id;
    private final String owner;
    private final ExpenseType type;
    private final String description;
    private final LocalDate date;
    private final double amount;

    private Expense(Long id,
                    String owner,
                    ExpenseType type,
                    String description,
                    LocalDate date,
                    double amount) {
        this.id = id;
        this.owner = owner;
        this.type = type;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    @JsonCreator
    public static Builder newBuilder() {
        return new Builder();
    }

    @JsonProperty("id")
    public Long id() {
        return id;
    }

    @JsonProperty("owner")
    String owner() {
        return owner;
    }

    @JsonProperty("type")
    ExpenseType type() {
        return type;
    }

    @Nullable
    @JsonProperty("description")
    String description() {
        return description;
    }

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    public LocalDate date() {
        return date;
    }

    @JsonProperty("amount")
    double amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        return Double.compare(expense.amount, amount) == 0 &&
                Objects.equals(owner, expense.owner) &&
                type == expense.type &&
                Objects.equals(description, expense.description) &&
                Objects.equals(date, expense.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, type, description, date, amount);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("owner", owner)
                .add("type", type)
                .add("description", description)
                .add("date", date)
                .add("amount", amount)
                .toString();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private Long id;
        private String owner;
        private ExpenseType type;
        private String description;
        private LocalDate date;
        private Double amount;

        @JsonProperty("id")
        Builder id(Long id) {
            this.id = id;
            return this;
        }

        @JsonProperty("owner")
        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        @JsonProperty("type")
        public Builder type(ExpenseType type) {
            this.type = type;
            return this;
        }

        @JsonProperty("description")
        @Nullable
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        @JsonProperty("date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        @JsonProperty("amount")
        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Expense build() {
            Preconditions.checkNotNull(owner);
            Preconditions.checkNotNull(type);
            Preconditions.checkNotNull(date);
            Preconditions.checkNotNull(amount);

            return new Expense(id, owner, type, description, date, amount);
        }
    }
}

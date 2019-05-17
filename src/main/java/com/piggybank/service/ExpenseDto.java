package com.piggybank.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.piggybank.model.ExpenseType;

import java.time.LocalDate;
import java.util.Optional;

@AutoValue
@JsonDeserialize(builder = ExpenseDto.Builder.class)
public abstract class ExpenseDto {
    public static Builder newBuilder() {
        return AutoValue_ExpenseDto.Builder.newBuilder();
    }

    @JsonProperty("owner")
    public abstract String owner();

    @JsonProperty("type")
    public abstract ExpenseType type();

    @JsonProperty("description")
    public abstract Optional<String> description();

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    public abstract LocalDate date();

    @JsonProperty("amount")
    public abstract double amount();

    @JsonPOJOBuilder
    @AutoValue.Builder
    public abstract static class Builder {

        @JsonCreator
        static Builder newBuilder() {
            return new AutoValue_ExpenseDto.Builder();
        }

        @JsonProperty("owner")
        public abstract Builder owner(String owner);

        @JsonProperty("type")
        public abstract Builder type(ExpenseType type);

        @JsonProperty("description")
        public abstract Builder description(String description);

        @JsonProperty("date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        public abstract Builder date(LocalDate date);

        @JsonProperty("amount")
        public abstract Builder amount(double amount);

        public abstract ExpenseDto build();
    }
}

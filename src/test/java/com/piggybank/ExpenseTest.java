package com.piggybank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class ExpenseTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldBuildAnExpense() throws Exception {
        final String initialJson = " {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"id\" : \"12345\",\n" +
                "        \"type\" : \"MOTORBIKE\",\n" +
                "        \"date\" : \"20181127\",\n" +
                "        \"amount\" : \"24.5\",\n" +
                "        \"description\": \"Benzina\"\n" +
                "    }";


        Assert.assertEquals(Expense.newBuilder()
                .owner("example@example.it")
                .id(12345L)
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .description("Benzina")
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build(), mapper.readValue(initialJson, Expense.class));
    }

    @Test
    public void shouldBuildAnExpenseWithoutDescription() throws Exception {
        final String initialJson = " {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"id\" : \"12345\",\n" +
                "        \"type\" : \"MOTORBIKE\",\n" +
                "        \"date\" : \"20181127\",\n" +
                "        \"amount\" : \"24.5\"" +
                "    }";

        Assert.assertEquals(Expense.newBuilder()
                .owner("example@example.it")
                .id(12345L)
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build(), mapper.readValue(initialJson, Expense.class));
    }

    @Test
    public void shouldBuildAListOfExpenses() throws Exception {
        final String initialJson = "[\n" +
                "    {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"id\" : \"12345\",\n" +
                "        \"type\" : \"MOTORBIKE\",\n" +
                "        \"date\" : \"20181127\",\n" +
                "        \"amount\": 24.5,\n" +
                "        \"description\": \"Benzina\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"id\" : \"54321\",\n" +
                "        \"type\" : \"HOUSE\",\n" +
                "        \"date\" : \"20181129\",\n" +
                "        \"amount\": 400,\n" +
                "        \"description\": \"Affitto\"\n" +
                "    }\n" +
                "]";

        final List<Expense> expected = Arrays.asList(
                Expense.newBuilder()
                        .owner("example@example.it")
                        .id(12345L)
                        .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                        .description("Benzina")
                        .type(ExpenseType.MOTORBIKE)
                        .amount(24.5)
                        .build(),
                Expense.newBuilder()
                        .owner("example@example.it")
                        .id(54321L)
                        .date(LocalDate.of(2018, Month.NOVEMBER, 29))
                        .description("Affitto")
                        .type(ExpenseType.HOUSE)
                        .amount(400)
                        .build());

        Assert.assertEquals(expected, mapper.readValue(initialJson, new TypeReference<List<Expense>>() {
        }));
    }

}

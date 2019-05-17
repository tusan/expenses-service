package com.piggybank.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.piggybank.model.ExpenseType;
import com.piggybank.service.ExpenseDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class ExpenseDtoTest {
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
                "        \"type\" : \"MOTORBIKE\",\n" +
                "        \"date\" : \"20181127\",\n" +
                "        \"amount\" : \"24.5\",\n" +
                "        \"description\": \"Benzina\"\n" +
                "    }";


        Assert.assertEquals(ExpenseDto.newBuilder()
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .description("Benzina")
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build(), mapper.readValue(initialJson, ExpenseDto.class));
    }

    @Test
    public void shouldBuildAnExpenseWithoutDescription() throws Exception {
        final String initialJson = " {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"type\" : \"MOTORBIKE\",\n" +
                "        \"date\" : \"20181127\",\n" +
                "        \"amount\" : \"24.5\"" +
                "    }";

        Assert.assertEquals(ExpenseDto.newBuilder()
                .owner("example@example.it")
                .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                .type(ExpenseType.MOTORBIKE)
                .amount(24.5)
                .build(), mapper.readValue(initialJson, ExpenseDto.class));
    }

    @Test
    public void shouldBuildAListOfExpenses() throws Exception {
        final String initialJson = "[\n" +
                "    {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"type\" : \"MOTORBIKE\",\n" +
                "        \"date\" : \"20181127\",\n" +
                "        \"amount\": 24.5,\n" +
                "        \"description\": \"Benzina\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"owner\" : \"example@example.it\",\n" +
                "        \"type\" : \"HOUSE\",\n" +
                "        \"date\" : \"20181129\",\n" +
                "        \"amount\": 400,\n" +
                "        \"description\": \"Affitto\"\n" +
                "    }\n" +
                "]";

        final List<ExpenseDto> expected = Arrays.asList(
                ExpenseDto.newBuilder()
                        .owner("example@example.it")
                        .date(LocalDate.of(2018, Month.NOVEMBER, 27))
                        .description("Benzina")
                        .type(ExpenseType.MOTORBIKE)
                        .amount(24.5)
                        .build(),
                ExpenseDto.newBuilder()
                        .owner("example@example.it")
                        .date(LocalDate.of(2018, Month.NOVEMBER, 29))
                        .description("Affitto")
                        .type(ExpenseType.HOUSE)
                        .amount(400)
                        .build()
        );

        Assert.assertEquals(expected, mapper.readValue(initialJson, new TypeReference<List<ExpenseDto>>() {
        }));
    }

}

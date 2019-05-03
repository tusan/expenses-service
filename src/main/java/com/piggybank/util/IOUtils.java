package com.piggybank.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class IOUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static String readFileFromClassPath(String fileName) {
        return new BufferedReader(
                new InputStreamReader(requireNonNull(IOUtils.class
                        .getClassLoader()
                        .getResourceAsStream(fileName))))
                .lines()
                .collect(Collectors.joining());
    }

    public static <T> String serialize(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] value, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(value, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(InputStream value, TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(value, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

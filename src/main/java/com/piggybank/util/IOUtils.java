package com.piggybank.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.piggybank.util.ExceptionUtils.wrapCheckedException;

public class IOUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.registerModule(new Jdk8Module());
    }

    public static Optional<String> readFileFromClassPath(String fileName) {
        final Optional<InputStream> resource = Optional.ofNullable(IOUtils.class
                .getClassLoader()
                .getResourceAsStream(fileName));

        return resource.map(res -> new BufferedReader(
                new InputStreamReader(res))
                .lines()
                .collect(Collectors.joining()));

    }

    public static <T> String serialize(T obj) {
        return wrapCheckedException(() -> OBJECT_MAPPER.writeValueAsString(obj));
    }

    public static <T> T deserialize(byte[] value, Class<T> type) {
        return wrapCheckedException(() -> OBJECT_MAPPER.readValue(value, type));
    }

    public static <T> T deserialize(InputStream value, TypeReference<T> type) {
        return wrapCheckedException(() -> OBJECT_MAPPER.readValue(value, type));
    }
}

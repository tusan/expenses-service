package com.piggybank.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class IOUtils {
    public static String inputStreamToString(InputStream reader) {
        return new BufferedReader(
                new InputStreamReader(reader))
                .lines()
                .collect(Collectors.joining());
    }
}

package com.piggybank;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class ExpenseAppTest {

    @Test
    public void shouldRunTheEmbeddedServer() throws Exception {
        final ExpensesApp sut = new ExpensesApp();
        sut.start();

        URL url = new URL("http://localhost:8080");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Assert.assertEquals(200, con.getResponseCode());
        Assert.assertEquals("Hello World!", parseResponse(con));
    }

    private String parseResponse(HttpURLConnection con) throws IOException {

        return new BufferedReader(
                new InputStreamReader(con.getInputStream()))
                .lines()
                .collect(Collectors.joining());
    }
}

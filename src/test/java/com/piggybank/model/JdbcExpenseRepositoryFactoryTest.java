package com.piggybank.model;

import com.piggybank.context.JdbcConnectionProvider;
import com.piggybank.util.MapBasedConfReader;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collections;

public class JdbcExpenseRepositoryFactoryTest {
    private JdbcExpenseRepositoryFactory sut;

    @Test
    public void shouldBuildASingletonObject() {
        sut = new JdbcExpenseRepositoryFactory(new JdbcConnectionProvider(new MapBasedConfReader(Collections.emptyMap())) {
            @Override
            public Connection forCurrentConfigs() {
                return null;
            }
        });

        Assert.assertEquals(sut.getRepository(), sut.getRepository());
    }
}
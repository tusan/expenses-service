package com.piggybank.util;

import java.util.concurrent.Callable;

public class ExceptionUtils {
    public static <T> T wrapCheckedException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

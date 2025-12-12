package com.example.framework;

public class TestDatabaseManagerException extends RuntimeException {
    public TestDatabaseManagerException(String message) {
        super(message);
    }

    public TestDatabaseManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}

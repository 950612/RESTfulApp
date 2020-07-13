package com.example.RESTapp.exception;

public class PrimaryKeyWasNotFoundException extends RuntimeException {
    public PrimaryKeyWasNotFoundException() {
        super("Primary key was not found in file");
    }
}

package com.example.RESTapp.exception;

public class IncorrectFileExtensionOrSizeException extends RuntimeException {
    public IncorrectFileExtensionOrSizeException() {
        super("File must be text and not empty ");
    }
}

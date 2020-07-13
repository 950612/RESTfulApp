package com.example.RESTapp.exception;

public class NotPrimaryKeyException extends RuntimeException {
    public NotPrimaryKeyException() { super("Primary key in some record is blank!"); }
}

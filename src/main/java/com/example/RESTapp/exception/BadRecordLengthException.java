package com.example.RESTapp.exception;

public class BadRecordLengthException extends RuntimeException {
    public BadRecordLengthException(){ super("Some record length is invalid! Greater than 4 elements"); }
}

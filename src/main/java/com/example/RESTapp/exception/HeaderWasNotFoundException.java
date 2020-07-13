package com.example.RESTapp.exception;

public class HeaderWasNotFoundException extends RuntimeException {
    public HeaderWasNotFoundException() { super("Obligatory header was not found!"); }}

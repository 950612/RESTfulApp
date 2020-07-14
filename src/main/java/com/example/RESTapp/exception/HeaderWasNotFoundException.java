package com.example.RESTapp.exception;

public class HeaderWasNotFoundException extends RuntimeException {
    public HeaderWasNotFoundException() { super("Obligatory header was not found or your file has the other encoding than UTF-8/ANSI"); }}

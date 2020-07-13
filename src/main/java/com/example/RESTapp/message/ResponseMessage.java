package com.example.RESTapp.message;

/**
 * The model of the return response gives to client
 */
public class ResponseMessage {
    private String message;
    private String httpStatus;

    public ResponseMessage(String message, String httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getHttpStatus() {
        return httpStatus;
    }
}

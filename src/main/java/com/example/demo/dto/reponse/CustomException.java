package com.example.demo.dto.reponse;

public class CustomException extends RuntimeException {
    private String message;
    private String errorCode;

    public CustomException(String message, String errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

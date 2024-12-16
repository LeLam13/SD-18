package com.example.demo.dto.reponse;

public class ErrorResponseDTO {
    private String error;
    private String errorCode;

    // Constructor, getters, setters
    public ErrorResponseDTO(String error, String errorCode) {
        this.error = error;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

package com.ayush.discussionforum.dto;


public enum ExceptionResponse {

    MISSING_REQUIRED_FIELD("Missing field! Not allowed"),
    RECORD_ALREADY_EXISTS(""),
    AUTHENTICATION_FAILED("");

    private String errorMessage;

    ExceptionResponse(String msg){
        errorMessage = msg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

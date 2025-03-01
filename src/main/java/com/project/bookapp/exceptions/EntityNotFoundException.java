package com.project.bookapp.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private String field;
    private String message;

    public EntityNotFoundException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    // Getters for the field and message
    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

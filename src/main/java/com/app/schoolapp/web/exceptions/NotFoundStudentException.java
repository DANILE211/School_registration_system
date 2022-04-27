package com.app.schoolapp.web.exceptions;

public class NotFoundStudentException extends RuntimeException{
    public NotFoundStudentException(String message) {
        super(message);
    }
}

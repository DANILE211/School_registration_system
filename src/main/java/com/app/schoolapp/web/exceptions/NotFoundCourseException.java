package com.app.schoolapp.web.exceptions;

public class NotFoundCourseException extends RuntimeException{
    public NotFoundCourseException(String message) {
        super(message);
    }
}

package com.yura.lampak.model;


public class TaskException extends Exception {

    public TaskException() {
    }

    public TaskException(String message) { super(message); }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.svym.inventory.service.exception;

public class DuplicatePatientIdException extends RuntimeException {
    public DuplicatePatientIdException(String message) {
        super(message);
    }
}

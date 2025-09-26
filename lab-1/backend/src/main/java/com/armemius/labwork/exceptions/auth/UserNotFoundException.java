package com.armemius.labwork.exceptions.auth;

import com.armemius.labwork.exceptions.LabworkException;

public class UserNotFoundException extends LabworkException {

    public UserNotFoundException(String message) {
        super(message);
    }
}

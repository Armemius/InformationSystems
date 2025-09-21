package com.armemius.labwork.exceptions.auth;

import com.armemius.labwork.exceptions.LabworkException;

public class UsernameTakenException extends LabworkException {
    public UsernameTakenException(String message) {
        super(message);
    }
}

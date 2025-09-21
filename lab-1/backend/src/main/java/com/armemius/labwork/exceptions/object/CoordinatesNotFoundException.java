package com.armemius.labwork.exceptions.object;

import com.armemius.labwork.exceptions.LabworkException;

public class CoordinatesNotFoundException extends LabworkException {
    public CoordinatesNotFoundException(String message) {
        super(message);
    }
}

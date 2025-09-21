package com.armemius.labwork.exceptions.object;

import com.armemius.labwork.exceptions.LabworkException;

public class LocationNotFoundException extends LabworkException {
    public LocationNotFoundException(String message) {
        super(message);
    }
}

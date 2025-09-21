package com.armemius.labwork.exceptions.object;

import com.armemius.labwork.exceptions.LabworkException;

public class AddressNotFoundException extends LabworkException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}

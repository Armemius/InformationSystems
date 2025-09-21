package com.armemius.labwork.exceptions.object;

import com.armemius.labwork.exceptions.LabworkException;

public class OrganizationNotFoundException extends ObjectNotFoundException {
    public OrganizationNotFoundException(String message) {
        super(message);
    }
}

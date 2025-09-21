package com.armemius.labwork.service.abstractions;

import com.armemius.labwork.data.domain.AppUser;

public interface Owned {
    void setOwner(AppUser owner);
    AppUser getOwner();
}

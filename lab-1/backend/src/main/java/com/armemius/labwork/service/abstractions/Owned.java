package com.armemius.labwork.service.abstractions;

import com.armemius.labwork.data.domain.AppUser;

public interface Owned {

    AppUser getOwner();

    void setOwner(AppUser owner);
}

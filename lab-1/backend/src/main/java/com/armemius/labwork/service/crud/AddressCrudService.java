package com.armemius.labwork.service.crud;

import com.armemius.labwork.data.domain.Address;
import com.armemius.labwork.data.dto.objects.AddressDto;
import com.armemius.labwork.data.repository.AddressRepository;
import com.armemius.labwork.service.abstractions.CrudService;
import com.armemius.labwork.service.mapper.AddressMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressCrudService extends CrudService<Address, AddressDto> {

    @Autowired
    public AddressCrudService(AddressRepository repository, AddressMapperService mapper) {
        super(repository, mapper);
    }

    @Override
    protected String[] getIgnoredPropertiesForUpdate() {
        return new String[]{
                "id",
                "owner",
                "correspondingOrganizationsWithPostalAddress",
                "correspondingOrganizationsWithOfficialAddress"
        };
    }
}

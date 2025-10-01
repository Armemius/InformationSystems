package com.armemius.labwork.service.crud;

import com.armemius.labwork.data.domain.Location;
import com.armemius.labwork.data.dto.objects.LocationDto;
import com.armemius.labwork.data.repository.LocationRepository;
import com.armemius.labwork.service.abstractions.CrudService;
import com.armemius.labwork.service.mapper.LocationMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationCrudService extends CrudService<Location, LocationDto> {

    @Autowired
    public LocationCrudService(LocationRepository repository, LocationMapperService mapper) {
        super(repository, mapper);
    }

    @Override
    protected String[] getIgnoredPropertiesForUpdate() {
        return new String[]{"id", "owner", "correspondingTowns"};
    }
}

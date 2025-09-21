package com.armemius.labwork.service.crud;

import com.armemius.labwork.data.domain.Coordinates;
import com.armemius.labwork.data.dto.objects.CoordinatesDto;
import com.armemius.labwork.data.repository.CoordinatesRepository;
import com.armemius.labwork.service.abstractions.CrudService;
import com.armemius.labwork.service.mapper.CoordinatesMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoordinatesCrudService extends CrudService<Coordinates, CoordinatesDto> {

    @Autowired
    public CoordinatesCrudService(CoordinatesRepository repository, CoordinatesMapperService mapper) {
        super(repository, mapper);
    }

    @Override
    protected String[] getIgnoredPropertiesForUpdate() {
        return new String[]{"id", "owner", "correspondingOrganizations"};
    }
}

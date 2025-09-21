package com.armemius.labwork.service.crud;

import com.armemius.labwork.data.domain.Organization;
import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.data.repository.OrganizationRepository;
import com.armemius.labwork.service.abstractions.CrudService;
import com.armemius.labwork.service.mapper.OrganizationMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationCrudService extends CrudService<Organization, OrganizationDto> {

    @Autowired
    public OrganizationCrudService(OrganizationRepository repository, OrganizationMapperService mapper) {
        super(repository, mapper);
    }

}

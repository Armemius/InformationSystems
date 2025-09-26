package com.armemius.labwork.service.crud;

import com.armemius.labwork.data.domain.Organization;
import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.data.repository.OrganizationRepository;
import com.armemius.labwork.service.abstractions.CrudService;
import com.armemius.labwork.service.mapper.OrganizationMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrganizationCrudService extends CrudService<Organization, OrganizationDto> {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationCrudService(OrganizationRepository repository, OrganizationMapperService mapper) {
        super(repository, mapper);
        this.organizationRepository = repository;
    }

    public Page<OrganizationDto> getAllWithFilter(PageRequest page, String filter) {
        if (filter == null || filter.isEmpty()) {
            return super.getAll(page);
        }
        return organizationRepository.findAllByName(filter, page).map(mapper::toDto);
    }
}

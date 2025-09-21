package com.armemius.labwork.service;

import com.armemius.labwork.data.domain.Address;
import com.armemius.labwork.data.domain.Organization;
import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.data.repository.AddressRepository;
import com.armemius.labwork.data.repository.OrganizationRepository;
import com.armemius.labwork.exceptions.LabworkException;
import com.armemius.labwork.exceptions.object.AddressNotFoundException;
import com.armemius.labwork.exceptions.object.OrganizationNotFoundException;
import com.armemius.labwork.service.mapper.OrganizationMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapperService mapper;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public List<OrganizationDto> findByNameStartsWith(String prefix) {
        return organizationRepository.findAll().stream()
                .filter(org -> org.getName() != null && org.getName().startsWith(prefix))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationDto> findByRatingLessThan(Float maxRating) {
        return organizationRepository.findAll().stream()
                .filter(org -> org.getRating() != null && org.getRating() < maxRating)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationDto> findByRatingGreaterThan(Float minRating) {
        return organizationRepository.findAll().stream()
                .filter(org -> org.getRating() != null && org.getRating() > minRating)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationDto mergeOrganizations(Long firstOrgId, Long secondOrgId, String newName, Long addressId) {
        Organization first = organizationRepository.findById(firstOrgId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + firstOrgId + " not found"));
        Organization second = organizationRepository.findById(secondOrgId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + secondOrgId + " not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address with id " + addressId + " not found"));

        Organization merged = new Organization();
        merged.setName(newName);
        merged.setOfficialAddress(address);
        merged.setCreationDate(new Date());

        merged.setEmployeesCount(first.getEmployeesCount() + second.getEmployeesCount());

        merged.setAnnualTurnover(first.getAnnualTurnover() + second.getAnnualTurnover());
        merged.setRating((first.getRating() + second.getRating()) / 2);

        merged.setOwner(first.getOwner());

        merged.setCoordinates(first.getCoordinates());
        merged.setPostalAddress(first.getPostalAddress());
        merged.setType(first.getType());

        Organization saved = organizationRepository.save(merged);
        return mapper.toDto(saved);
    }

    @Transactional
    public OrganizationDto absorbOrganization(Long absorberId, Long absorbedId) {
        if (absorberId.equals(absorbedId)) {
            throw new LabworkException("Organization cannot absorb itself");
        }

        Organization absorber = organizationRepository.findById(absorberId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + absorberId + " not found"));
        Organization absorbed = organizationRepository.findById(absorbedId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + absorbedId + " not found"));

        absorber.setEmployeesCount(absorber.getEmployeesCount() + absorbed.getEmployeesCount());
        absorber.setAnnualTurnover(absorber.getAnnualTurnover() + absorbed.getAnnualTurnover());
        absorber.setRating((absorber.getRating() + absorbed.getRating()) / 2);
        organizationRepository.delete(absorbed);
        Organization updated = organizationRepository.save(absorber);

        return mapper.toDto(updated);
    }
}



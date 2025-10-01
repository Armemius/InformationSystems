package com.armemius.labwork.service.mapper;

import com.armemius.labwork.data.domain.Address;
import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.domain.Coordinates;
import com.armemius.labwork.data.domain.Organization;
import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.data.repository.AddressRepository;
import com.armemius.labwork.data.repository.AppUserRepository;
import com.armemius.labwork.data.repository.CoordinatesRepository;
import com.armemius.labwork.exceptions.auth.UserNotFoundException;
import com.armemius.labwork.exceptions.object.AddressNotFoundException;
import com.armemius.labwork.exceptions.object.CoordinatesNotFoundException;
import com.armemius.labwork.service.abstractions.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationMapperService implements MapperService<OrganizationDto, Organization> {

    private final AppUserRepository appUserRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final AddressRepository addressRepository;

    @Override
    public OrganizationDto toDto(Organization entity) {
        if (entity == null) {
            return null;
        }
        OrganizationDto dto = new OrganizationDto();
        dto.setId(entity.getId());
        dto.setOwnerId(entity.getOwner() != null ? entity.getOwner().getId() : null);
        dto.setName(entity.getName());
        dto.setCoordinatesId(entity.getCoordinates() != null ? entity.getCoordinates().getId() : null);
        dto.setCreationDate(entity.getCreationDate());
        dto.setOfficialAddressId(entity.getOfficialAddress() != null ? entity.getOfficialAddress().getId() : null);
        dto.setAnnualTurnover(entity.getAnnualTurnover());
        dto.setEmployeesCount(entity.getEmployeesCount());
        dto.setRating(entity.getRating());
        dto.setType(entity.getType());
        dto.setPostalAddressId(entity.getPostalAddress() != null ? entity.getPostalAddress().getId() : null);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Organization toEntity(OrganizationDto dto) {
        if (dto == null) {
            return null;
        }
        Organization entity = new Organization();
        if (dto.getOwnerId() != null) {
            AppUser owner = appUserRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new UserNotFoundException("AppUser with ID " + dto.getOwnerId() + " not found"));
            entity.setOwner(owner);
        }
        if (dto.getCoordinatesId() != null) {
            Coordinates coordinates = coordinatesRepository.findById(dto.getCoordinatesId())
                    .orElseThrow(() -> new CoordinatesNotFoundException("Coordinates with ID " + dto.getCoordinatesId() + " not found"));
            entity.setCoordinates(coordinates);
        }
        entity.setName(dto.getName());
        entity.setCreationDate(dto.getCreationDate());
        entity.setAnnualTurnover(dto.getAnnualTurnover() != null ? dto.getAnnualTurnover() : 0f);
        entity.setEmployeesCount(dto.getEmployeesCount() != null ? dto.getEmployeesCount() : 0);
        entity.setRating(dto.getRating());
        entity.setType(dto.getType());
        if (dto.getOfficialAddressId() != null) {
            Address officialAddress = addressRepository.findById(dto.getOfficialAddressId())
                    .orElseThrow(() -> new AddressNotFoundException("Address with ID " + dto.getOfficialAddressId() + " not found"));
            entity.setOfficialAddress(officialAddress);
        }
        if (dto.getPostalAddressId() != null) {
            Address postalAddress = addressRepository.findById(dto.getPostalAddressId())
                    .orElseThrow(() -> new AddressNotFoundException("Address with ID " + dto.getPostalAddressId() + " not found"));
            entity.setPostalAddress(postalAddress);
        }
        return entity;
    }
}

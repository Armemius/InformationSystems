package com.armemius.labwork.service.mapper;

import com.armemius.labwork.data.domain.Address;
import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.domain.Location;
import com.armemius.labwork.data.dto.objects.AddressDto;
import com.armemius.labwork.data.repository.AppUserRepository;
import com.armemius.labwork.data.repository.LocationRepository;
import com.armemius.labwork.exceptions.auth.UserNotFoundException;
import com.armemius.labwork.exceptions.object.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressMapperService {

    private final AppUserRepository appUserRepository;
    private final LocationRepository locationRepository;

    public AddressDto toDto(Address entity) {
        if (entity == null) {
            return null;
        }
        AddressDto dto = new AddressDto();
        dto.setId(entity.getId());
        dto.setOwnerId(entity.getOwner() != null ? entity.getOwner().getId() : null);
        dto.setStreet(entity.getStreet());
        dto.setZipCode(entity.getZipCode());
        dto.setTownId(entity.getTown() != null ? entity.getTown().getId() : null);
        return dto;
    }

    @Transactional(readOnly = true)
    public Address toEntity(AddressDto dto) {
        if (dto == null) {
            return null;
        }
        Address entity = new Address();
        if (dto.getOwnerId() != null) {
            AppUser owner = appUserRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new UserNotFoundException("AppUser with ID " + dto.getOwnerId() + " not found"));
            entity.setOwner(owner);
        }
        if (dto.getTownId() != null) {
            Location town = locationRepository.findById(dto.getTownId())
                    .orElseThrow(() -> new LocationNotFoundException("Location with ID " + dto.getTownId() + " not found"));
            entity.setTown(town);
        }
        entity.setStreet(dto.getStreet());
        entity.setZipCode(dto.getZipCode());
        return entity;
    }
}

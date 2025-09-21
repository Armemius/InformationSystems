package com.armemius.labwork.service.mapper;

import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.domain.Location;
import com.armemius.labwork.data.dto.objects.LocationDto;
import com.armemius.labwork.data.repository.AppUserRepository;
import com.armemius.labwork.exceptions.auth.UserNotFoundException;
import com.armemius.labwork.service.abstractions.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationMapperService implements MapperService<LocationDto, Location> {
    private final AppUserRepository appUserRepository;

    @Override
    public LocationDto toDto(Location entity) {
        if (entity == null) {
            return null;
        }
        LocationDto dto = new LocationDto();
        dto.setId(entity.getId());
        dto.setOwnerId(entity.getOwner() != null ? entity.getOwner().getId() : null);
        dto.setX(entity.getX());
        dto.setY(entity.getY());
        dto.setZ(entity.getZ());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Location toEntity(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        Location entity = new Location();
        if (dto.getOwnerId() != null) {
            AppUser owner = appUserRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new UserNotFoundException("AppUser with ID " + dto.getOwnerId() + " not found"));
            entity.setOwner(owner);
        }
        entity.setX(dto.getX() != null ? dto.getX() : 0);
        entity.setY(dto.getY() != null ? dto.getY() : 0f);
        entity.setZ(dto.getZ() != null ? dto.getZ() : 0.0);
        return entity;
    }
}

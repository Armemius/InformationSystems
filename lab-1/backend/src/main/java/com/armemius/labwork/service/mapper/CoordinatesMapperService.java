package com.armemius.labwork.service.mapper;

import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.domain.Coordinates;
import com.armemius.labwork.data.dto.objects.CoordinatesDto;
import com.armemius.labwork.data.repository.AppUserRepository;
import com.armemius.labwork.exceptions.auth.UserNotFoundException;
import com.armemius.labwork.service.abstractions.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoordinatesMapperService implements MapperService<CoordinatesDto, Coordinates> {

    private final AppUserRepository appUserRepository;

    @Override
    public CoordinatesDto toDto(Coordinates entity) {
        if (entity == null) {
            return null;
        }
        CoordinatesDto dto = new CoordinatesDto();
        dto.setId(entity.getId());
        dto.setOwnerId(entity.getOwner() != null ? entity.getOwner().getId() : null);
        dto.setX(entity.getX());
        dto.setY(entity.getY());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Coordinates toEntity(CoordinatesDto dto) {
        if (dto == null) {
            return null;
        }
        Coordinates entity = new Coordinates();
        if (dto.getOwnerId() != null) {
            AppUser owner = appUserRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new UserNotFoundException("AppUser with ID " + dto.getOwnerId() + " not found"));
            entity.setOwner(owner);
        }
        entity.setX(dto.getX() != null ? dto.getX() : 0f);
        entity.setY(dto.getY());
        return entity;
    }
}

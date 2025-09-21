package com.armemius.labwork.service.abstractions;

import com.armemius.labwork.data.domain.AppUser;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class CrudService<K extends Owned, V> {

    protected final JpaRepository<K, Long> repository;
    protected final MapperService<V, K> mapper;

    public CrudService(JpaRepository<K, Long> repository, MapperService<V, K> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public V create(V dto, AppUser owner) {
        K entity = mapper.toEntity(dto);
        entity.setOwner(owner);
        K saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public V update(Long id, V dto) {
        K entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found with id " + id));
        updateEntityFromDto(entity, dto);
        K saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public V get(Long id) {
        K entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found with id " + id));
        return mapper.toDto(entity);
    }

    public Page<V> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    protected void updateEntityFromDto(K entity, V dto) {
        K temp = mapper.toEntity(dto);
        BeanUtils.copyProperties(temp, entity, getIgnoredPropertiesForUpdate());
    }

    protected String[] getIgnoredPropertiesForUpdate() {
        return new String[]{"id", "owner"};
    }

}

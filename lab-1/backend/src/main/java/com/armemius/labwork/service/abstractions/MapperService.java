package com.armemius.labwork.service.abstractions;

public interface MapperService<K, V> {

    V toEntity(K dto);

    K toDto(V entity);
}

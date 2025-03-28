package com.vuog.core.common.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectMappingUtil {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <D, T> D map(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> Optional<D> mapNullable(T entity, Class<D> outClass) {
        return Optional.ofNullable(modelMapper.map(entity, outClass));
    }

    public static <D, T> List<D> mapAll(Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }

    public static <D, T> Page<D> mapAll(Page<T> entityPage, Class<D> outCLass) {
        return entityPage.map(entity -> map(entity, outCLass));
    }

}

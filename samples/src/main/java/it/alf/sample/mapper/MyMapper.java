package it.alf.sample.mapper;

import org.mapstruct.Mapper;

import it.alf.baseframework.mapper.GenericMapper;
import it.alf.sample.dto.MyDto;
import it.alf.sample.model.MyEntity;

/**
 * MapStruct mapper that converts between `MyEntity` and `MyDto`.
 *
 * How it relates to the framework:
 * - The `baseframework` provides a `GenericMapper<E,D>` contract that
 *   application mappers can extend. This keeps the mapping contract
 *   consistent across projects and lets developers rely on the same
 *   mapper method names.
 * - Using `@Mapper(componentModel = "spring")` tells MapStruct to
 *   generate a Spring bean for the mapper, so it can be injected into
 *   services or controllers if manual mapping is needed.
 */
@Mapper(componentModel = "spring")
public interface MyMapper extends GenericMapper<MyEntity, MyDto> {
    // MapStruct will generate implementations for these conversions.
    MyEntity toEntity(MyDto dto);
    MyDto toDto(MyEntity entity);
}

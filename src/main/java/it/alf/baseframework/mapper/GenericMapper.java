package it.alf.baseframework.mapper;

import java.util.List;

import it.alf.baseframework.dto.BaseDto;
import it.alf.baseframework.model.BaseEntity;

/**
 * Generic mapper interface to be extended by concrete MapStruct mappers in
 * application modules. MapStruct will generate implementations for concrete
 * sub-interfaces that provide concrete type parameters.
 *
 * Example in application:
 *
 * @Mapper(componentModel = "spring")
 * public interface MyEntityMapper extends GenericMapper<MyEntity, MyDto> {}
 */
public interface GenericMapper<E extends BaseEntity, D extends BaseDto> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entities);

}

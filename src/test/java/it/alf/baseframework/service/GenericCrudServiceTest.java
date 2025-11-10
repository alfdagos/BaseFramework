package it.alf.baseframework.service;

import it.alf.baseframework.model.BaseEntity;
import it.alf.baseframework.repository.GenericRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GenericCrudServiceTest {

    @Test
    void createEntity_ShouldReturnSavedEntity() {
    GenericRepository<BaseEntity> mockRepository = mock(GenericRepository.class);
    GenericCrudService<BaseEntity> service = new GenericCrudService<BaseEntity>(mockRepository) {};
        BaseEntity entity = new BaseEntity();

        when(mockRepository.save(entity)).thenReturn(entity);
    // repository injected via constructor in the anonymous subclass

        BaseEntity created = service.create(entity);
        assertThat(created).isEqualTo(entity);
    }

    @Test
    void findById_ShouldReturnEntity() {
    GenericRepository<BaseEntity> mockRepository = mock(GenericRepository.class);
    GenericCrudService<BaseEntity> service = new GenericCrudService<BaseEntity>(mockRepository) {};
        BaseEntity entity = new BaseEntity();
        entity.setId(1L);

        when(mockRepository.findById(1L)).thenReturn(Optional.of(entity));
    // repository injected via constructor in the anonymous subclass

        Optional<BaseEntity> found = service.findById(1L);
        assertThat(found).isPresent().contains(entity);
    }
}

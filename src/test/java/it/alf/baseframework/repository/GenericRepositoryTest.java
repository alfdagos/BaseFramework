package it.alf.baseframework.repository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.alf.baseframework.model.BaseEntity;

public class GenericRepositoryTest {

    @Test
    void repositoryMockShouldSaveAndFindEntity() {
        GenericRepository<BaseEntity> repository = mock(GenericRepository.class);
        BaseEntity entity = new BaseEntity();

        when(repository.save(entity)).thenReturn(entity);
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));

        BaseEntity savedEntity = repository.save(entity);
        List<BaseEntity> entities = repository.findAll();

        assertThat(savedEntity).isNotNull();
        assertThat(entities).contains(savedEntity);
    }
}

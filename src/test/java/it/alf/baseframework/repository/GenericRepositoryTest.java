package it.alf.baseframework.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.alf.baseframework.testsupport.SampleEntity;

class GenericRepositoryTest {

    @Test
    @SuppressWarnings("unchecked")
    void repositoryMockShouldSaveAndFindEntity() {
        GenericRepository<SampleEntity> repository = mock(GenericRepository.class);
        SampleEntity entity = new SampleEntity("a");

        when(repository.save(entity)).thenReturn(entity);
        when(repository.findAll()).thenReturn(List.of(entity));

        SampleEntity savedEntity = repository.save(entity);
        List<SampleEntity> entities = repository.findAll();

        assertThat(savedEntity).isNotNull();
        assertThat(entities).contains(savedEntity);
    }
}

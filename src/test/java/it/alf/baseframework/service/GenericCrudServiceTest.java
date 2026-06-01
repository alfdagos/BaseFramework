package it.alf.baseframework.service;

import it.alf.baseframework.exception.ResourceNotFoundException;
import it.alf.baseframework.repository.GenericRepository;
import it.alf.baseframework.testsupport.SampleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GenericCrudServiceTest {

    private GenericRepository<SampleEntity> repository;
    private GenericCrudService<SampleEntity> service;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        repository = mock(GenericRepository.class);
        service = new GenericCrudService<>(repository) {
        };
    }

    @Test
    void create_shouldReturnSavedEntity() {
        SampleEntity entity = new SampleEntity("a");
        when(repository.save(entity)).thenReturn(entity);

        assertThat(service.create(entity)).isEqualTo(entity);
        verify(repository).save(entity);
    }

    @Test
    void findById_shouldReturnEntity() {
        SampleEntity entity = new SampleEntity("a");
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThat(service.findById(1L)).contains(entity);
    }

    @Test
    void getById_shouldThrowWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_shouldThrowWhenEntityDoesNotExist() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(5L, new SampleEntity("x")))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void update_shouldCarryOverIdAuditAndVersionFromExisting() {
        SampleEntity existing = new SampleEntity("old");
        existing.setId(7L);
        existing.setVersion(3L);
        existing.setCreatedAt(Instant.parse("2020-01-01T00:00:00Z"));
        when(repository.findById(7L)).thenReturn(Optional.of(existing));

        SampleEntity incoming = new SampleEntity("new");
        when(repository.save(incoming)).thenReturn(incoming);

        SampleEntity updated = service.update(7L, incoming);

        // id, version and createdAt are carried over so save() merges (UPDATE) and audit state is kept.
        assertThat(updated.getId()).isEqualTo(7L);
        assertThat(updated.getVersion()).isEqualTo(3L);
        assertThat(updated.getCreatedAt()).isEqualTo(Instant.parse("2020-01-01T00:00:00Z"));
        verify(repository).save(incoming);
    }

    @Test
    void delete_shouldThrowWhenEntityDoesNotExist() {
        when(repository.existsById(3L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(3L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void delete_shouldRemoveWhenEntityExists() {
        when(repository.existsById(2L)).thenReturn(true);

        service.delete(2L);

        verify(repository).deleteById(2L);
    }
}

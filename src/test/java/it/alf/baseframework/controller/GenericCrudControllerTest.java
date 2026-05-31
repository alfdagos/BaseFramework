package it.alf.baseframework.controller;

import it.alf.baseframework.service.GenericCrudService;
import it.alf.baseframework.testsupport.SampleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GenericCrudControllerTest {

    private GenericCrudController<SampleEntity> controllerFor(GenericCrudService<SampleEntity> service) {
        return new GenericCrudController<>(service) {
        };
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_shouldReturnListOfEntities() {
        GenericCrudService<SampleEntity> service = mock(GenericCrudService.class);
        SampleEntity entity = new SampleEntity("a");
        when(service.findAll()).thenReturn(List.of(entity));

        ResponseEntity<List<SampleEntity>> response = controllerFor(service).findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(entity);
    }

    @Test
    @SuppressWarnings("unchecked")
    void create_shouldReturn201WithLocationHeader() {
        GenericCrudService<SampleEntity> service = mock(GenericCrudService.class);
        SampleEntity saved = new SampleEntity("a");
        saved.setId(42L);
        when(service.create(any())).thenReturn(saved);

        ResponseEntity<SampleEntity> response = controllerFor(service).create(new SampleEntity("a"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasToString("/42");
        assertThat(response.getBody()).isEqualTo(saved);
    }

    @Test
    @SuppressWarnings("unchecked")
    void delete_shouldReturn204() {
        GenericCrudService<SampleEntity> service = mock(GenericCrudService.class);

        ResponseEntity<Void> response = controllerFor(service).delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service).delete(1L);
    }
}

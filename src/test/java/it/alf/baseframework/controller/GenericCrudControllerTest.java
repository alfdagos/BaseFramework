package it.alf.baseframework.controller;

import it.alf.baseframework.model.BaseEntity;
import it.alf.baseframework.service.GenericCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GenericCrudControllerTest {

    @Test
    void findAll_ShouldReturnListOfEntities() {
    GenericCrudService<BaseEntity> mockService = mock(GenericCrudService.class);
    GenericCrudController<BaseEntity> controller = new GenericCrudController<BaseEntity>(mockService) {};
        BaseEntity entity = new BaseEntity();

        when(mockService.findAll()).thenReturn(Collections.singletonList(entity));
    // service injected via constructor in the anonymous subclass

        ResponseEntity<List<BaseEntity>> response = controller.findAll();
        assertThat(response.getBody()).contains(entity);
    }
}

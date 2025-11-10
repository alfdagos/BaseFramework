package it.alf.sample.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.alf.baseframework.controller.GenericCrudController;
import it.alf.sample.model.MyEntity;
import it.alf.sample.service.MyEntityService;

/**
 * REST controller exposing CRUD endpoints for MyEntity.
 *
 * This controller demonstrates the recommended pattern when using the
 * `baseframework` generic controller:
 * - Extend `GenericCrudController<T>` to inherit default endpoint behavior
 *   (POST, PUT, DELETE, GET) and avoid writing repetitive boilerplate.
 * - Annotate the concrete subclass with `@RestController` and provide a
 *   specific `@RequestMapping` so that each resource has its own path.
 *
 * The concrete controller is intentionally thin — it delegates to the
 * `GenericCrudService` implementation (in this sample `MyEntityService`).
 */
@RestController
@RequestMapping("/api/my-entities")
public class MyEntityController extends GenericCrudController<MyEntity> {
    /**
     * The service is injected via constructor and passed to the parent
     * generic controller which knows how to call it for CRUD operations.
     */
    public MyEntityController(MyEntityService service) {
        super(service);
    }
}

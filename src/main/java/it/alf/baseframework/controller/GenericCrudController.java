package it.alf.baseframework.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import it.alf.baseframework.model.BaseEntity;
import it.alf.baseframework.service.GenericCrudService;
import jakarta.validation.Valid;

/**
 * Abstract generic REST controller exposing CRUD endpoints.
 *
 * <p>It is intentionally NOT annotated with {@code @RestController} / {@code @RequestMapping}
 * so the concrete controller owns the resource path and bean registration:
 *
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/my-entities")
 * public class MyEntityController extends GenericCrudController<MyEntity> {
 *     public MyEntityController(MyEntityService service) { super(service); }
 * }
 * }</pre>
 *
 * <p>Semantics:
 * <ul>
 *   <li>{@code POST} -> 201 Created with a {@code Location} header;</li>
 *   <li>{@code PUT /{id}} -> 200 OK (404 if the resource does not exist);</li>
 *   <li>{@code DELETE /{id}} -> 204 No Content (404 if it does not exist);</li>
 *   <li>{@code GET /{id}} -> 200 OK (404 if it does not exist);</li>
 *   <li>{@code GET} -> 200 OK with the full list;</li>
 *   <li>{@code GET /page} -> 200 OK with a {@link Page}.</li>
 * </ul>
 *
 * @param <T> the managed entity type
 */
public abstract class GenericCrudController<T extends BaseEntity> {

    protected final GenericCrudService<T> service;

    protected GenericCrudController(GenericCrudService<T> service) {
        this.service = service;
    }

    @Operation(summary = "Create a new resource")
    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
        T created = service.create(entity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Replace an existing resource")
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable("id") Long id, @Valid @RequestBody T entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @Operation(summary = "Delete a resource by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Fetch a single resource by id")
    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "List all resources")
    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "List resources with pagination and sorting")
    @GetMapping("/page")
    public ResponseEntity<Page<T>> findPage(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }
}

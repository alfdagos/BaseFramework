package it.alf.baseframework.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import it.alf.baseframework.model.BaseEntity;
import it.alf.baseframework.service.GenericCrudService;

/**
 * Abstract generic REST controller exposing CRUD endpoints.
 *
 * Important: this class is intentionally NOT annotated with @RestController or
 * @RequestMapping so that the concrete controller can decide the request path
 * and controller registration. Example:
 *
 * @RestController
 * @RequestMapping("/api/my-entities")
 * public class MyEntityController extends GenericCrudController<MyEntity> { ... }
 */
public abstract class GenericCrudController<T extends BaseEntity> {

    protected final GenericCrudService<T> service;

    protected GenericCrudController(GenericCrudService<T> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        return new ResponseEntity<>(service.create(entity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable Long id, @RequestBody T entity) {
        return new ResponseEntity<>(service.update(id, entity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
}

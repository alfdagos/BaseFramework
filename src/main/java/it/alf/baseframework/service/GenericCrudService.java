package it.alf.baseframework.service;

import java.util.List;
import java.util.Optional;

import it.alf.baseframework.model.BaseEntity;
import it.alf.baseframework.repository.GenericRepository;

/**
 * Abstract generic CRUD service. Intended to be extended by application-specific services.
 *
 * Note: this class does not declare a Spring stereotype annotation so that the
 * concrete subclass can decide how to register the bean (e.g. with @Service).
 */
public abstract class GenericCrudService<T extends BaseEntity> {

    protected final GenericRepository<T> repository;

    protected GenericCrudService(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public T update(Long id, T entity) {
        entity.setId(id);
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }
}

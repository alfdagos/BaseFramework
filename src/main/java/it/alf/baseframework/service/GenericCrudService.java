package it.alf.baseframework.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import it.alf.baseframework.exception.ResourceNotFoundException;
import it.alf.baseframework.model.BaseEntity;
import it.alf.baseframework.repository.GenericRepository;

/**
 * Abstract generic CRUD service, intended to be extended by application-specific services.
 *
 * <p>It deliberately declares no Spring stereotype so the concrete subclass decides how the
 * bean is registered (typically with {@code @Service}). Write operations are transactional
 * and {@code update}/{@code delete} validate existence, throwing
 * {@link ResourceNotFoundException} (mapped to HTTP 404) instead of silently up-serting.
 *
 * @param <T> the managed entity type
 */
public abstract class GenericCrudService<T extends BaseEntity> {

    protected final GenericRepository<T> repository;

    protected GenericCrudService(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @Transactional
    public T create(T entity) {
        return repository.save(entity);
    }

    @Transactional
    public T update(Long id, T entity) {
        T existing = getById(id);
        entity.setId(id);
        // Carry over the audit/version state so {@code save()} performs a merge (UPDATE)
        // instead of a persist (INSERT). With a wrapper @Version column Spring Data treats an
        // entity whose version is null as new; a PUT payload never carries the version, so
        // without this the row would be re-inserted and createdAt lost.
        entity.setVersion(existing.getVersion());
        entity.setCreatedAt(existing.getCreatedAt());
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        requireExisting(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Returns the entity or throws {@link ResourceNotFoundException} when it does not exist.
     */
    @Transactional(readOnly = true)
    public T getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of(entityName(), id));
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    private void requireExisting(Long id) {
        if (!repository.existsById(id)) {
            throw ResourceNotFoundException.of(entityName(), id);
        }
    }

    /**
     * Human-readable name used in {@link ResourceNotFoundException} messages. The default
     * is the simple class name of this service with the {@code Service} suffix removed;
     * subclasses may override it for a nicer label.
     */
    protected String entityName() {
        return getClass().getSimpleName().replaceFirst("Service$", "");
    }
}

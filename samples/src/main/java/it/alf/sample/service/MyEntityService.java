package it.alf.sample.service;

import org.springframework.stereotype.Service;

import it.alf.baseframework.service.GenericCrudService;
import it.alf.sample.model.MyEntity;
import it.alf.sample.repository.MyEntityRepository;

/**
 * Concrete service for MyEntity.
 *
 * Important patterns shown here:
 * - The framework provides `GenericCrudService<T>` as an abstract base with
 *   common CRUD logic. Consumers should extend it and pass their domain
 *   repository via constructor injection (as shown below). This keeps the
 *   framework free of concrete beans while still offering shared behavior.
 * - The class is annotated with `@Service` so Spring can pick it up as a bean.
 */
@Service
public class MyEntityService extends GenericCrudService<MyEntity> {
    /**
     * Pass the domain-specific repository to the parent generic service.
     * Constructor injection is the recommended approach to make dependencies
     * explicit and to ease testing.
     */
    public MyEntityService(MyEntityRepository repository) {
        super(repository);
    }
}

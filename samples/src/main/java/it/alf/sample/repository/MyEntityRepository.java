package it.alf.sample.repository;

import it.alf.baseframework.repository.GenericRepository;
import it.alf.sample.model.MyEntity;

/**
 * Repository interface for MyEntity. By extending `GenericRepository<MyEntity>` from
 * the `baseframework` we automatically inherit standard CRUD and paging methods.
 *
 * Why this helps:
 * - The `baseframework` centralizes the generic DAO contract so every microservice
 *   can declare only the domain-specific repository interface and reuse the
 *   same behavior.
 */
public interface MyEntityRepository extends GenericRepository<MyEntity> {
}

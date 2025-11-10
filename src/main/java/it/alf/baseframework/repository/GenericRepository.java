package it.alf.baseframework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.alf.baseframework.model.BaseEntity;

public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long> {}

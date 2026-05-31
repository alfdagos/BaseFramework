package it.alf.baseframework.testsupport;

import it.alf.baseframework.model.BaseEntity;
import jakarta.persistence.Entity;

/**
 * Minimal concrete entity used across the framework unit/slice tests, since
 * {@link BaseEntity} is abstract and cannot be instantiated directly.
 */
@Entity
public class SampleEntity extends BaseEntity {

    private String name;

    public SampleEntity() {
    }

    public SampleEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

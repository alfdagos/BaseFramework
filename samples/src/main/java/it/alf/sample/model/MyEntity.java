package it.alf.sample.model;

import it.alf.baseframework.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Sample domain entity.
 *
 * <p>By extending {@link BaseEntity} it inherits the surrogate id, auditing timestamps and the
 * optimistic-locking version, so only the domain-specific fields remain here. Bean Validation
 * constraints are honoured by the generic controller, which annotates request bodies with
 * {@code @Valid} — invalid payloads are rejected with an RFC 7807 {@code 400} response produced
 * by the framework's {@code GlobalExceptionHandler}.
 */
@Entity
public class MyEntity extends BaseEntity {

    @NotBlank(message = "name must not be blank")
    @Size(max = 255, message = "name must not exceed 255 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

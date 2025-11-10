package it.alf.sample.model;

import it.alf.baseframework.model.BaseEntity;
import jakarta.persistence.Entity;

@Entity
public class MyEntity extends BaseEntity {
    /**
     * Domain field for the sample entity.
     *
     * Why extend BaseEntity?
     * - `BaseEntity` provided by the `baseframework` defines the common
     *   id property and any shared auditing fields (if present). Extending
     *   it avoids repeating this boilerplate in each microservice entity.
     */
    private String name;

    // Plain getter/setter kept for clarity in the demo. In a real app you
    // may prefer Lombok or manually added convenience methods.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

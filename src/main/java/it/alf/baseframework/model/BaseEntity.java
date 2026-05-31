package it.alf.baseframework.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Base class for every persistent entity in a consuming microservice.
 *
 * <p>It centralizes the technical concerns that are identical across domains:
 * <ul>
 *   <li>a generated surrogate {@code id};</li>
 *   <li>auditing timestamps ({@code createdAt} / {@code updatedAt}) populated automatically
 *       by Spring Data JPA auditing (enabled by the framework auto-configuration);</li>
 *   <li>an optimistic-locking {@code version} column;</li>
 *   <li>an entity-correct {@link #equals(Object)} / {@link #hashCode()} implementation.</li>
 * </ul>
 *
 * <p>Concrete entities simply extend this class and add their domain fields.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Equality is based on the persistent identifier only. Two entities are considered
     * equal when they are of a compatible type and share a non-null id. Transient
     * entities (id still {@code null}) are only equal to themselves.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity other)) {
            return false;
        }
        return id != null && Objects.equals(id, other.id);
    }

    /**
     * A constant hash code keeps the contract stable while the id transitions from
     * {@code null} (transient) to a generated value (persisted), which is the
     * recommended approach for JPA entities.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", version=" + version + '}';
    }
}

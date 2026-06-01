package it.alf.baseframework.service;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import it.alf.baseframework.autoconfigure.BaseFrameworkJpaAuditingAutoConfiguration;
import it.alf.baseframework.testsupport.SampleEntity;
import it.alf.baseframework.testsupport.SampleEntityRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Persistence-level checks for {@link GenericCrudService#update(Long, it.alf.baseframework.model.BaseEntity)}
 * against a real (H2) {@code EntityManager}. These guard the merge-vs-persist behaviour that a
 * mock-based test cannot exercise: a PUT payload never carries the optimistic-lock version, and
 * without carrying it over Spring Data would treat the entity as new and INSERT a duplicate row
 * (losing the original {@code createdAt}) instead of updating in place.
 */
@DataJpaTest
@Import(BaseFrameworkJpaAuditingAutoConfiguration.class)
class GenericCrudServicePersistenceTest {

    @Autowired
    private SampleEntityRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private GenericCrudService<SampleEntity> service;

    @BeforeEach
    void setUp() {
        service = new GenericCrudService<>(repository) {
        };
    }

    @Test
    void updateMergesInPlaceAndPreservesCreatedAt() {
        SampleEntity created = service.create(new SampleEntity("original"));
        entityManager.flush();
        Long id = created.getId();
        assertThat(created.getCreatedAt()).isNotNull();

        // Detach and reload so we compare against the value actually stored in the column
        // (the timestamp(6) round-trip rounds the in-memory Instant's extra nanos), and so the
        // update goes through the same detached-entity path a real PUT would.
        entityManager.clear();
        Instant persistedCreatedAt = repository.findById(id).orElseThrow().getCreatedAt();
        entityManager.clear();

        SampleEntity updated = service.update(id, new SampleEntity("updated"));
        entityManager.flush();

        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getName()).isEqualTo("updated");
        assertThat(updated.getCreatedAt()).isEqualTo(persistedCreatedAt);
        // A merge updates the existing row; a persist would have left two rows behind.
        assertThat(repository.findAll()).hasSize(1);
    }
}

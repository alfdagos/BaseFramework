package it.alf.baseframework.model;

import it.alf.baseframework.autoconfigure.BaseFrameworkJpaAuditingAutoConfiguration;
import it.alf.baseframework.testsupport.SampleEntity;
import it.alf.baseframework.testsupport.SampleEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that {@link BaseEntity} auditing fields and optimistic-lock version are populated
 * by the framework's JPA auditing auto-configuration.
 */
@DataJpaTest
@Import(BaseFrameworkJpaAuditingAutoConfiguration.class)
class BaseEntityAuditingTest {

    @Autowired
    private SampleEntityRepository repository;

    @Test
    void auditingFieldsAndVersionArePopulatedOnPersist() {
        SampleEntity saved = repository.saveAndFlush(new SampleEntity("audited"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getVersion()).isZero();
    }

    @Test
    void equalsIsBasedOnId() {
        SampleEntity a = new SampleEntity("a");
        SampleEntity b = new SampleEntity("b");
        a.setId(1L);
        b.setId(1L);

        assertThat(a).isEqualTo(b);
        assertThat(new SampleEntity("x")).isNotEqualTo(new SampleEntity("x")); // both transient
    }
}

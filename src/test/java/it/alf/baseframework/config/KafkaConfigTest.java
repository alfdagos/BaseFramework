package it.alf.baseframework.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;

@SpringBootTest(classes = KafkaConfig.class)
public class KafkaConfigTest {

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Test
    void consumerFactoryShouldNotBeNull() {
        assertThat(consumerFactory).isNotNull();
    }
}

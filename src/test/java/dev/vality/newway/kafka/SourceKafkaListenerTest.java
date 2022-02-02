package dev.vality.newway.kafka;

import dev.vality.newway.config.KafkaPostgresqlSpringBootITest;
import dev.vality.newway.service.SourceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyList;

@KafkaPostgresqlSpringBootITest
public class SourceKafkaListenerTest {

    @Value("${kafka.topics.source.id}")
    public String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private SourceService sourceService;

    @Test
    public void listenEmptyChanges() {
        kafkaProducer.sendMessage(topic);
        Mockito.verify(sourceService, Mockito.timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .handleEvents(anyList());
    }

}

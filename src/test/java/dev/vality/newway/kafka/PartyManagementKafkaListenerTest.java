package dev.vality.newway.kafka;

import dev.vality.newway.config.KafkaPostgresqlSpringBootITest;
import dev.vality.newway.service.PartyManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@KafkaPostgresqlSpringBootITest
public class PartyManagementKafkaListenerTest {

    @Value("${kafka.topics.party-management.id}")
    public String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private PartyManagementService partyManagementService;

    @Test
    public void listenEmptyChanges() {
        kafkaProducer.sendMessage(topic);
        verify(partyManagementService, timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .handleEvents(anyList());
    }

}

package dev.vality.newway.kafka;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.TestData;
import dev.vality.newway.config.KafkaPostgresqlSpringBootITest;
import dev.vality.newway.dao.withdrawal.iface.FistfulCashFlowDao;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalAdjustmentDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

@KafkaPostgresqlSpringBootITest
@TestPropertySource(properties = {"kafka.topics.withdrawal-adjustment.enabled=true"})
class WithdrawalKafkaListenerWithoutAdjustmentTest {

    @Value("${kafka.topics.withdrawal.id}")
    public String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private WithdrawalAdjustmentDao withdrawalAdjustmentDao;

    @MockBean
    private FistfulCashFlowDao fistfulCashFlowDao;

    @BeforeEach
    void setUp() {
        Mockito.reset(withdrawalAdjustmentDao);
        Mockito.reset(fistfulCashFlowDao);
    }

    @Test
    void ignoreWithdrawalAdjustmentCreatedChangeWhileFilterEnabled() {
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentCreatedChange("adjustmentId");
        MachineEvent message = new MachineEvent();
        message.setCreatedAt("2023-07-03T10:15:30Z");
        message.setEventId(1L);
        message.setSourceNs("sourceNs");
        message.setSourceId("sourceId");
        message.setData(dev.vality.machinegun.msgpack.Value.bin(new ThriftSerializer<>().serialize("", timestampedChange)));

        kafkaProducer.sendMessage(topic, message);

        Mockito.verify(withdrawalAdjustmentDao, Mockito.after(TimeUnit.MINUTES.toMillis(1)).only())
                .save(any());
    }
}

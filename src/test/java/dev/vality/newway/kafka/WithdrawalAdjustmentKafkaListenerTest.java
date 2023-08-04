package dev.vality.newway.kafka;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.TestData;
import dev.vality.newway.config.KafkaPostgresqlSpringBootITest;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalAdjustmentDao;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentStatus;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;

@KafkaPostgresqlSpringBootITest
class WithdrawalAdjustmentKafkaListenerTest {

    @Value("${kafka.topics.withdrawal.id}")
    public String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private WithdrawalAdjustmentDao withdrawalAdjustmentDao;

    @Test
    void listenWithdrawalAdjustmentCreatedChange() {
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentCreatedStatusChange();
        MachineEvent message = new MachineEvent();
        message.setCreatedAt("2023-07-03T10:15:30Z");
        message.setEventId(1L);
        message.setSourceNs("sourceNs");
        message.setSourceId("sourceId");
        message.setData(dev.vality.machinegun.msgpack.Value.bin(new ThriftSerializer<>().serialize("", timestampedChange)));

        kafkaProducer.sendMessage(topic, message);

        Mockito.verify(withdrawalAdjustmentDao, Mockito.timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .save(any());
    }

    @Test
    void listenWithdrawalAdjustmentStatusChange() {
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentStatusChange();
        MachineEvent message = new MachineEvent();
        message.setCreatedAt("2023-07-03T10:15:30Z");
        message.setEventId(1L);
        message.setSourceNs("sourceNs");
        message.setSourceId("sourceId");
        message.setData(dev.vality.machinegun.msgpack.Value.bin(new ThriftSerializer<>().serialize("", timestampedChange)));
        WithdrawalAdjustment withdrawalAdjustment = new WithdrawalAdjustment();
        withdrawalAdjustment.setAdjustmentId("id");
        withdrawalAdjustment.setStatus(WithdrawalAdjustmentStatus.pending);
        withdrawalAdjustment.setCurrent(true);
        withdrawalAdjustment.setId(1L);
        Mockito.when(withdrawalAdjustmentDao.getById(anyString())).thenReturn(withdrawalAdjustment);
        Mockito.when(withdrawalAdjustmentDao.save(any(WithdrawalAdjustment.class))).thenReturn(Optional.of(1L));

        kafkaProducer.sendMessage(topic, message);

        Mockito.verify(withdrawalAdjustmentDao, Mockito.timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .getById(any());
        Mockito.verify(withdrawalAdjustmentDao, Mockito.timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .save(any());
        Mockito.verify(withdrawalAdjustmentDao, Mockito.timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .updateNotCurrent(anyLong());
    }

}

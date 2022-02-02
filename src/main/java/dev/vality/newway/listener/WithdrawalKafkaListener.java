package dev.vality.newway.listener;

import dev.vality.kafka.common.util.LogUtil;
import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.newway.service.WithdrawalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WithdrawalKafkaListener {

    private final WithdrawalService withdrawalService;

    @KafkaListener(
            autoStartup = "${kafka.topics.withdrawal.enabled}",
            topics = "${kafka.topics.withdrawal.id}",
            containerFactory = "withdrawalContainerFactory")
    public void handle(List<ConsumerRecord<String, SinkEvent>> messages, Acknowledgment ack) {
        log.info("Got machineEvent batch with size: {}", messages.size());
        withdrawalService.handleEvents(messages.stream()
                .map(m -> m.value().getEvent())
                .collect(Collectors.toList()));
        ack.acknowledge();
        log.info("Batch has been committed, size={}, {}", messages.size(),
                LogUtil.toSummaryStringWithSinkEventValues(messages));
    }
}

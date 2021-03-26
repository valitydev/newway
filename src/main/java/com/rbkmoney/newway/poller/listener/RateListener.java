package com.rbkmoney.newway.poller.listener;

import com.rbkmoney.machinegun.eventsink.SinkEvent;
import com.rbkmoney.newway.service.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RateListener {

    private final RateService rateService;

    @KafkaListener(topics = "${kafka.topics.rate.id}", containerFactory = "rateContainerFactory")
    public void handle(List<ConsumerRecord<String, SinkEvent>> messages, Acknowledgment ack) {
        log.info("Got RateListener messages batch with size: {}", messages.size());
        rateService.handleEvents(
                messages.stream()
                        .map(ConsumerRecord::value)
                        .collect(Collectors.toList())
        );
        ack.acknowledge();
        log.info("Batch RateListener has been committed, size={}", messages.size());
    }
}
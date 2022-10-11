package dev.vality.newway.kafka;

import dev.vality.exrates.base.Rational;
import dev.vality.exrates.events.Currency;
import dev.vality.exrates.events.CurrencyEvent;
import dev.vality.exrates.events.CurrencyEventPayload;
import dev.vality.exrates.events.CurrencyExchangeRate;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.newway.config.KafkaPostgresqlSpringBootITest;
import dev.vality.newway.dao.exrate.iface.ExchangeRateDao;
import dev.vality.newway.service.ExchangeRateService;
import org.apache.thrift.TBase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@KafkaPostgresqlSpringBootITest
public class ExchangeRateKafkaListenerTest {

    @Value("${kafka.topics.exrate.id}")
    public String topic;

    @Autowired
    private dev.vality.testcontainers.annotations.kafka.config.KafkaProducer<TBase<?, ?>> testThriftKafkaProducer;

    @SpyBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateDao exchangeRateDao;

    @Test
    public void listenExchangeRateEventTest() {
        // Given
        CurrencyEvent currencyEvent = buildCurrencyEvent();

        // When
        testThriftKafkaProducer.send(topic, currencyEvent);

        // Then
        verify(exchangeRateService, timeout(TimeUnit.MINUTES.toMillis(1)).times(1)).handleEvents(anyList());
    }

    private CurrencyEvent buildCurrencyEvent() {
        CurrencyEvent currencyEvent = new CurrencyEvent();
        currencyEvent.setEventId(UUID.randomUUID().toString());
        currencyEvent.setEventCreatedAt(TypeUtil.temporalToString(LocalDateTime.now()));
        currencyEvent.setPayload(
                CurrencyEventPayload.exchange_rate(
                        new CurrencyExchangeRate(
                                new Currency("USD", (short) 2),
                                new Currency("RUB", (short) 2),
                                new Rational(60797502, 1000000),
                                TypeUtil.temporalToString(Instant.now())
                        )
                )
        );

        return currencyEvent;
    }

}
package dev.vality.newway.config;

import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.config.properties.KafkaConsumerProperties;
import dev.vality.newway.config.properties.KafkaSslProperties;
import dev.vality.newway.serde.PayoutEventDeserializer;
import dev.vality.newway.serde.SinkEventDeserializer;
import dev.vality.payout.manager.Event;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaSslProperties.class)
public class KafkaConfig {

    private final KafkaConsumerProperties kafkaConsumerProperties;

    @Value("${kafka.topics.party-management.consumer.group-id}")
    private String partyConsumerGroup;
    @Value("${kafka.client-id}")
    private String clientId;
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, MachineEvent> consumerFactory(KafkaSslProperties kafkaSslProperties) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(kafkaSslProperties));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getInvoicingConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> recPayToolContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getRecurrentPaymentToolConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> rateContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getRateConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> depositContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getDepositConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> identityContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getIdentityConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> walletContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getWalletConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> withdrawalContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getWithdrawalConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Event> payoutContainerFactory(
            KafkaSslProperties kafkaSslProperties) {
        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, Event>(consumerConfigs(kafkaSslProperties));
        kafkaConsumerFactory.setValueDeserializer(new PayoutEventDeserializer());
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Event>();
        initFactory(kafkaConsumerFactory, kafkaConsumerProperties.getPayoutConcurrency(), factory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> sourceContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getSourceConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> destinationContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getDestinationConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> withdrawalSessionContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory) {
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getWithdrawalSessionConcurrency());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> partyManagementContainerFactory(
            KafkaSslProperties kafkaSslProperties) {
        Map<String, Object> configs = consumerConfigs(kafkaSslProperties);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, partyConsumerGroup);
        ConsumerFactory<String, MachineEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(configs);
        return createConcurrentFactory(consumerFactory, kafkaConsumerProperties.getPartyManagementConcurrency());
    }

    private Map<String, Object> consumerConfigs(KafkaSslProperties kafkaSslProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SinkEventDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroupId());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerProperties.isEnableAutoCommit());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties.getAutoOffsetReset());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerProperties.getMaxPollRecords());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerProperties.getSessionTimeoutMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerProperties.getMaxPollIntervalMs());
        configureSsl(props, kafkaSslProperties);
        return props;
    }

    private void configureSsl(Map<String, Object> props, KafkaSslProperties kafkaSslProperties) {
        if (kafkaSslProperties.isEnabled()) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name());
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                    new File(kafkaSslProperties.getTrustStoreLocation()).getAbsolutePath());
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaSslProperties.getTrustStorePassword());
            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, kafkaSslProperties.getKeyStoreType());
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, kafkaSslProperties.getTrustStoreType());
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
                    new File(kafkaSslProperties.getKeyStoreLocation()).getAbsolutePath());
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaSslProperties.getKeyStorePassword());
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, kafkaSslProperties.getKeyPassword());
        }
    }

    private ConcurrentKafkaListenerContainerFactory<String, MachineEvent> createConcurrentFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory,
            int threadsNumber) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, MachineEvent>();
        initFactory(consumerFactory, threadsNumber, factory);
        return factory;
    }

    private <T> void initFactory(
            ConsumerFactory<String, T> consumerFactory,
            int threadsNumber,
            ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        var defaultErrorHandler = new DefaultErrorHandler(new ExponentialBackOff());
        defaultErrorHandler.setAckAfterHandle(false);
        factory.setCommonErrorHandler(defaultErrorHandler);
        factory.setConcurrency(threadsNumber);
    }
}

package dev.vality.newway.config;

import dev.vality.newway.kafka.KafkaProducer;
import dev.vality.testcontainers.annotations.DefaultSpringBootTest;
import dev.vality.testcontainers.annotations.kafka.KafkaTestcontainerSingleton;
import dev.vality.testcontainers.annotations.postgresql.PostgresqlTestcontainerSingleton;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PostgresqlTestcontainerSingleton
@KafkaTestcontainerSingleton(
        properties = {
                "kafka.topics.invoice.enabled=true",
                "kafka.topics.recurrent-payment-tool.enabled=true",
                "kafka.topics.party-management.enabled=true",
                "kafka.topics.rate.enabled=true",
                "kafka.topics.identity.enabled=true",
                "kafka.topics.wallet.enabled=true",
                "kafka.topics.deposit.enabled=true",
                "kafka.topics.withdrawal.enabled=true",
                "kafka.topics.withdrawal-session.enabled=true",
                "kafka.topics.source.enabled=true",
                "kafka.topics.destination.enabled=true",
                "kafka.topics.pm-events-payout.enabled=true"},
        topicsKeys = {
                "kafka.topics.invoice.id",
                "kafka.topics.recurrent-payment-tool.id",
                "kafka.topics.party-management.id",
                "kafka.topics.rate.id",
                "kafka.topics.identity.id",
                "kafka.topics.wallet.id",
                "kafka.topics.deposit.id",
                "kafka.topics.withdrawal.id",
                "kafka.topics.withdrawal-session.id",
                "kafka.topics.source.id",
                "kafka.topics.destination.id",
                "kafka.topics.pm-events-payout.id"})
@DefaultSpringBootTest
@Import(KafkaProducer.class)
public @interface KafkaPostgresqlSpringBootITest {
}

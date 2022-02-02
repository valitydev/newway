package dev.vality.newway.handler.event.stock.impl.withdrawal.session;

import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.config.KafkaPostgresqlSpringBootITest;
import dev.vality.newway.domain.tables.pojos.Destination;
import dev.vality.newway.domain.tables.pojos.WithdrawalSession;
import dev.vality.newway.utils.WithdrawalSessionCreatedHandlerUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

import static dev.vality.newway.utils.WithdrawalSessionCreatedHandlerUtils.createSession;

@KafkaPostgresqlSpringBootITest
public class WithdrawalSessionCreatedDigitalWalletHandlerTest {

    @Autowired
    private WithdrawalSessionCreatedHandler withdrawalSessionCreatedHandler;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Destination destination = dev.vality.testcontainers.annotations.util.RandomBeans.random(Destination.class);
    String sqlStatement = "select * from nw.withdrawal_session LIMIT 1;";

    @BeforeEach
    public void setUp() {
        destination.setCurrent(true);
    }

    @Test
    public void digitalWalletTest() {
        dev.vality.fistful.base.Resource resource = new dev.vality.fistful.base.Resource();
        resource.setDigitalWallet(WithdrawalSessionCreatedHandlerUtils.createDestinationResourceDigitalWallet());
        dev.vality.fistful.withdrawal_session.Session session = createSession(resource);

        withdrawalSessionCreatedHandler.handle(
                WithdrawalSessionCreatedHandlerUtils.createCreated(session),
                WithdrawalSessionCreatedHandlerUtils.createCreatedMachineEvent(destination.getDestinationId(), session)
        );

        WithdrawalSession result = jdbcTemplate.queryForObject(sqlStatement,
                new RecordRowMapper<>(dev.vality.newway.domain.tables.WithdrawalSession.WITHDRAWAL_SESSION, WithdrawalSession.class));

        Assertions.assertNotNull(Objects.requireNonNull(result).getResourceDigitalWalletId());
        Assertions.assertNotNull(result.getResourceDigitalWalletData());
    }

}
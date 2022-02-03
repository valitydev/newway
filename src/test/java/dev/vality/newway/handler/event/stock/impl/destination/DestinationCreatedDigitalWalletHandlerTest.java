package dev.vality.newway.handler.event.stock.impl.destination;

import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.domain.tables.pojos.Destination;
import dev.vality.newway.utils.DestinationHandlerTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

import static dev.vality.newway.domain.tables.Destination.DESTINATION;

@PostgresqlSpringBootITest
public class DestinationCreatedDigitalWalletHandlerTest {

    @Autowired
    private DestinationCreatedHandler destinationCreatedHandler;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Destination destination = dev.vality.testcontainers.annotations.util.RandomBeans.random(Destination.class);
    String sqlStatement = "select * from nw.destination LIMIT 1;";

    @BeforeEach
    public void setUp() {
        destination.setCurrent(true);
    }

    @Test
    public void destinationCreatedHandlerTest() {
        dev.vality.fistful.base.Resource fistfulResource = new dev.vality.fistful.base.Resource();
        fistfulResource.setDigitalWallet(DestinationHandlerTestUtils.createResourceDigitalWallet());
        dev.vality.fistful.destination.Destination fistfulDestination
                = DestinationHandlerTestUtils.createFistfulDestination(fistfulResource);

        destinationCreatedHandler.handle(
                DestinationHandlerTestUtils.createCreated(fistfulDestination),
                DestinationHandlerTestUtils.createCreatedMachineEvent(
                        destination.getDestinationId(),
                        fistfulDestination
                ));

        Destination destinationResult = jdbcTemplate.queryForObject(sqlStatement,
                new RecordRowMapper<>(DESTINATION, Destination.class));

        Assertions.assertNotNull(Objects.requireNonNull(destinationResult).getResourceDigitalWalletId());
        Assertions.assertNotNull(destinationResult.getResourceDigitalWalletData());
    }

}
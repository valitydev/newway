package dev.vality.newway.service;

import dev.vality.limiter.config.LimitConfig;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.limiter.LimitConfigDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.UUID;

import static dev.vality.newway.domain.tables.LimitConfig.LIMIT_CONFIG;
import static dev.vality.newway.utils.LimitConfigGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

@PostgresqlSpringBootITest
public class LimitConfigServiceTest {

    private static final String TABLE_NAME = LIMIT_CONFIG.getSchema().getName() + "." + LIMIT_CONFIG.getName();

    @Autowired
    private LimitConfigService limitConfigService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LimitConfigDao limitConfigDao;

    @Test
    public void shouldHandleAndSave() {
        String limitConfigId = UUID.randomUUID().toString();
        LimitConfig limitConfig = getLimitConfig(limitConfigId);
        limitConfigService.handleEvents(List.of(
                getMachineEvent(limitConfigId, limitConfig), getMachineEvent(UUID.randomUUID().toString())));
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME))
                .isEqualTo(2);
        dev.vality.newway.domain.tables.pojos.LimitConfig limitConfig1 = limitConfigDao.get(limitConfigId);
        assertThat(limitConfig1.getShardSize())
                .isEqualTo(limitConfig.getShardSize());
    }

    private MachineEvent getMachineEvent(String limitConfigId) {
        return getMachineEvent(limitConfigId, getLimitConfig(limitConfigId));
    }

    private MachineEvent getMachineEvent(String limitConfigId, LimitConfig limitConfig) {
        return getEvent(
                limitConfigId,
                1,
                getCreated(limitConfig));
    }
}

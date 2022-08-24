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
import java.util.Set;
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
        var limitConfigId = UUID.randomUUID().toString();
        var limitConfig = getLimitConfig(limitConfigId);
        limitConfigService.handleEvents(List.of(
                getMachineEvent(limitConfigId, limitConfig), getMachineEvent(UUID.randomUUID().toString())));
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME))
                .isEqualTo(2);
        var saved = limitConfigDao.get(limitConfigId);
        assertThat(saved.getShardSize())
                .isEqualTo(limitConfig.getShardSize());
    }

    @Test
    public void shouldHandleAndSaveWithZeroScope() {
        var limitConfigId = UUID.randomUUID().toString();
        var limitConfig = getLimitConfig(limitConfigId);
        limitConfig.getScope().setMulti(Set.of());
        limitConfigService.handleEvents(List.of(getMachineEvent(limitConfigId, limitConfig)));
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME))
                .isEqualTo(1);
        var saved = limitConfigDao.get(limitConfigId);
        assertThat(saved.getShardSize())
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

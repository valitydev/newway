package dev.vality.newway.dao;

import dev.vality.limiter.config.LimitScopeType;
import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.dominant.impl.CountryDaoImpl;
import dev.vality.newway.dao.limiter.LimitConfigDao;
import dev.vality.newway.domain.tables.pojos.LimitConfig;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Set;
import java.util.stream.Collectors;

import static dev.vality.newway.utils.LimitConfigGenerator.getLimitConfig;
import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class LimitConfigDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LimitConfigDao limitConfigDao;

    @Autowired
    private CountryDaoImpl countryDao;

    @Test
    public void limitConfigDaoTest() {
        jdbcTemplate.execute("truncate table nw.limit_config cascade");
        var pojo = dev.vality.testcontainers.annotations.util.RandomBeans.random(LimitConfig.class);
        pojo.setCurrent(true);
        pojo.setLimitScopeTypesJson(getLimitScopeTypesJson(getLimitConfig(pojo.getLimitConfigId()).getScope().getMulti()));
        Long id = limitConfigDao.save(pojo).get();
        pojo.setId(id);
        var actual = limitConfigDao.get(pojo.getLimitConfigId());
        Assertions.assertEquals(pojo, actual);
        limitConfigDao.updateNotCurrent(actual.getId());
        limitConfigDao.save(pojo);
        assertThrows(NotFoundException.class, () -> limitConfigDao.get(pojo.getLimitConfigId()));
    }

    private String getLimitScopeTypesJson(Set<LimitScopeType> limitScopeTypes) {
        return JsonUtil.objectToJsonString(limitScopeTypes.stream()
                .map(JsonUtil::thriftBaseToJsonNode)
                .collect(Collectors.toList()));
    }
}

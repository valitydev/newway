package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.dominant.impl.CountryDaoImpl;
import dev.vality.newway.dao.limiter.LimitConfigDao;
import dev.vality.newway.domain.enums.LimitConfigLimitScopeType;
import dev.vality.newway.domain.tables.pojos.LimitConfig;
import dev.vality.newway.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

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
        pojo.setLimitScopeTypes(List.of(LimitConfigLimitScopeType.wallet, LimitConfigLimitScopeType.shop)
                .stream().map(LimitConfigLimitScopeType::getLiteral).collect(Collectors.joining(", ")));
        Long id = limitConfigDao.save(pojo).get();
        pojo.setId(id);
        var actual = limitConfigDao.get(pojo.getLimitConfigId());
        Assertions.assertEquals(pojo, actual);
        limitConfigDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        limitConfigDao.save(pojo);

        assertThrows(NotFoundException.class, () -> limitConfigDao.get(pojo.getLimitConfigId()));
    }
}

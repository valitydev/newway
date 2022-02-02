package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.identity.iface.IdentityDao;
import dev.vality.newway.domain.tables.pojos.Identity;
import dev.vality.newway.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class IdentityDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IdentityDao identityDao;

    @Test
    public void identityDaoTest() {
        jdbcTemplate.execute("truncate table nw.identity cascade");
        Identity identity = dev.vality.testcontainers.annotations.util.RandomBeans.random(Identity.class);
        identity.setCurrent(true);
        Long id = identityDao.save(identity).get();
        identity.setId(id);
        Identity actual = identityDao.get(identity.getIdentityId());
        Assertions.assertEquals(identity, actual);
        identityDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        identityDao.save(identity);

        assertThrows(NotFoundException.class, () -> identityDao.get(identity.getIdentityId()));
    }

}

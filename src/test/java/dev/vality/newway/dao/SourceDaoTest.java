package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.source.iface.SourceDao;
import dev.vality.newway.domain.tables.pojos.Source;
import dev.vality.newway.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class SourceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SourceDao sourceDao;

    @Test
    public void sourceDaoTest() {
        jdbcTemplate.execute("truncate table dw.source cascade");
        Source source = dev.vality.testcontainers.annotations.util.RandomBeans.random(Source.class);
        source.setCurrent(true);
        Long id = sourceDao.save(source).get();
        source.setId(id);
        Source actual = sourceDao.get(source.getSourceId());
        Assertions.assertEquals(source, actual);
        sourceDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        sourceDao.save(source);

        assertThrows(NotFoundException.class, () -> sourceDao.get(source.getSourceId()));
    }

}

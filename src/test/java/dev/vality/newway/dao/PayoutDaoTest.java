package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.payout.iface.PayoutDao;
import dev.vality.newway.domain.tables.pojos.Payout;
import dev.vality.newway.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class PayoutDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PayoutDao payoutDao;

    @Test
    public void payoutDaoTest() {
        jdbcTemplate.execute("truncate table nw.payout cascade");
        Payout payout = dev.vality.testcontainers.annotations.util.RandomBeans.random(Payout.class);
        payout.setCurrent(true);
        Optional<Long> save = payoutDao.save(payout);
        Payout payoutGet = payoutDao.get(payout.getPayoutId());
        Assertions.assertEquals(payout, payoutGet);
        payoutDao.updateNotCurrent(save.get());

        //check duplicate not error
        payoutDao.save(payout);

        assertThrows(NotFoundException.class, () -> payoutDao.get(payout.getPayoutId()));
    }
}

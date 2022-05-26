package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalDao;
import dev.vality.newway.domain.tables.pojos.Withdrawal;
import dev.vality.newway.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class WithdrawalDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WithdrawalDao withdrawalDao;

    @Test
    public void withdrawalDaoTest() {
        jdbcTemplate.execute("truncate table dw.withdrawal cascade");
        Withdrawal withdrawal = dev.vality.testcontainers.annotations.util.RandomBeans.random(Withdrawal.class);
        withdrawal.setCurrent(true);
        Long id = withdrawalDao.save(withdrawal).get();
        withdrawal.setId(id);
        Withdrawal actual = withdrawalDao.get(withdrawal.getWithdrawalId());
        Assertions.assertEquals(withdrawal, actual);
        withdrawalDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        withdrawalDao.save(withdrawal);

        assertThrows(NotFoundException.class, () -> withdrawalDao.get(withdrawal.getWithdrawalId()));
    }

}

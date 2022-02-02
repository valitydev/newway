package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.deposit.adjustment.iface.DepositAdjustmentDao;
import dev.vality.newway.domain.tables.pojos.DepositAdjustment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@PostgresqlSpringBootITest
public class DepositAdjustmentDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DepositAdjustmentDao depositAdjustmentDao;

    @Test
    public void depositAdjustmentTest() {
        DepositAdjustment deposit = dev.vality.testcontainers.annotations.util.RandomBeans.random(DepositAdjustment.class);
        deposit.setAmount(null);
        deposit.setCurrencyCode(null);
        deposit.setCurrent(true);
        Long id = depositAdjustmentDao.save(deposit).get();
        deposit.setId(id);
        DepositAdjustment actual = depositAdjustmentDao.get(deposit.getDepositId(), deposit.getAdjustmentId());
        Assertions.assertEquals(deposit, actual);
        depositAdjustmentDao.updateNotCurrent(actual.getId());
        Assertions.assertNull(depositAdjustmentDao.get(deposit.getDepositId(), deposit.getAdjustmentId()));

        //check duplicate not error
        depositAdjustmentDao.save(deposit);
    }

}

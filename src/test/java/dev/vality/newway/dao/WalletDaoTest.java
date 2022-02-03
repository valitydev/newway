package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.wallet.iface.WalletDao;
import dev.vality.newway.domain.tables.pojos.Wallet;
import dev.vality.newway.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertThrows;

@PostgresqlSpringBootITest
public class WalletDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WalletDao walletDao;

    @Test
    public void walletDaoTest() {
        jdbcTemplate.execute("truncate table nw.wallet cascade");
        Wallet wallet = dev.vality.testcontainers.annotations.util.RandomBeans.random(Wallet.class);
        wallet.setCurrent(true);
        Long id = walletDao.save(wallet).get();
        wallet.setId(id);
        Wallet actual = walletDao.get(wallet.getWalletId());
        Assertions.assertEquals(wallet, actual);
        walletDao.updateNotCurrent(actual.getId());

        //check duplicate not error
        walletDao.save(wallet);

        assertThrows(NotFoundException.class, () -> walletDao.get(wallet.getWalletId()));
    }

}

package dev.vality.newway.dao.wallet.iface;

import dev.vality.dao.GenericDao;
import dev.vality.newway.domain.tables.pojos.Wallet;
import dev.vality.newway.exception.DaoException;

import java.util.Optional;

public interface WalletDao extends GenericDao {

    Optional<Long> save(Wallet wallet) throws DaoException;

    Wallet get(String walletId) throws DaoException;

    void updateNotCurrent(Long id) throws DaoException;

}

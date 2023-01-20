package dev.vality.newway.dao.wallet.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.wallet.iface.WalletDao;
import dev.vality.newway.domain.tables.pojos.Wallet;
import dev.vality.newway.domain.tables.records.WalletRecord;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.newway.domain.tables.Wallet.WALLET;

@Component
public class WalletDaoImpl extends AbstractGenericDao implements WalletDao {

    private final RowMapper<Wallet> walletRowMapper;

    @Autowired
    public WalletDaoImpl(DataSource dataSource) {
        super(dataSource);
        walletRowMapper = new RecordRowMapper<>(WALLET, Wallet.class);
    }

    @Override
    public Optional<Long> save(Wallet wallet) throws DaoException {
        WalletRecord record = getDslContext().newRecord(WALLET, wallet);
        Query query = getDslContext()
                .insertInto(WALLET)
                .set(record)
                .onConflict(WALLET.WALLET_ID, WALLET.SEQUENCE_ID)
                .doNothing()
                .returning(WALLET.ID);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Wallet get(String walletId) throws DaoException {
        Query query = getDslContext().selectFrom(WALLET)
                .where(WALLET.WALLET_ID.eq(walletId)
                        .and(WALLET.CURRENT));
        return Optional.ofNullable(fetchOne(query, walletRowMapper))
                .orElseThrow(
                        () -> new NotFoundException(String.format("Wallet not found, walletId='%s'", walletId)));
    }

    @Override
    public void updateNotCurrent(Long walletId) throws DaoException {
        Query query = getDslContext().update(WALLET).set(WALLET.CURRENT, false)
                .where(WALLET.ID.eq(walletId)
                        .and(WALLET.CURRENT));
        execute(query);
    }

}

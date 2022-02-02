package dev.vality.newway.dao.party.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.mapper.RecordRowMapper;
import dev.vality.newway.dao.party.iface.ContractDao;
import dev.vality.newway.domain.tables.pojos.Contract;
import dev.vality.newway.domain.tables.records.ContractRecord;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import org.jooq.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static dev.vality.newway.domain.Tables.CONTRACT;

@Component
public class ContractDaoImpl extends AbstractGenericDao implements ContractDao {

    private final RowMapper<Contract> contractRowMapper;

    public ContractDaoImpl(DataSource dataSource) {
        super(dataSource);
        contractRowMapper = new RecordRowMapper<>(CONTRACT, Contract.class);
    }

    @Override
    public Optional<Long> save(Contract contract) throws DaoException {
        ContractRecord record = getDslContext().newRecord(CONTRACT, contract);
        Query query = getDslContext().insertInto(CONTRACT).set(record)
                .onConflict(CONTRACT.PARTY_ID, CONTRACT.CONTRACT_ID, CONTRACT.SEQUENCE_ID, CONTRACT.CHANGE_ID,
                        CONTRACT.CLAIM_EFFECT_ID)
                .doNothing()
                .returning(CONTRACT.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::longValue);
    }

    @NotNull
    @Override
    public Contract get(String partyId, String contractId) throws DaoException {
        Query query = getDslContext().selectFrom(CONTRACT)
                .where(CONTRACT.PARTY_ID.eq(partyId).and(CONTRACT.CONTRACT_ID.eq(contractId)).and(CONTRACT.CURRENT));

        return Optional.ofNullable(fetchOne(query, contractRowMapper))
                .orElseThrow(
                        () -> new NotFoundException(String.format("Contract not found, contractId='%s'", contractId)));
    }

    @Override
    public void updateNotCurrent(Long id) throws DaoException {
        Query query = getDslContext().update(CONTRACT).set(CONTRACT.CURRENT, false)
                .where(CONTRACT.ID.eq(id));
        executeOne(query);
    }
}

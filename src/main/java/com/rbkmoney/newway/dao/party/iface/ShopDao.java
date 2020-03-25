package com.rbkmoney.newway.dao.party.iface;

import com.rbkmoney.dao.GenericDao;
import com.rbkmoney.newway.domain.tables.pojos.Shop;
import com.rbkmoney.newway.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ShopDao extends GenericDao {
    Optional<Long> save(Shop shop) throws DaoException;

    Shop get(String partyId, String shopId) throws DaoException;

    void updateNotCurrent(Long shopId) throws DaoException;

    List<Shop> getByPartyId(String partyId);

    void saveWithUpdateCurrent(String partyId, Integer changeId, Shop shopSource, String shopId, long sequenceId, Long oldEventId, String eventName);
}

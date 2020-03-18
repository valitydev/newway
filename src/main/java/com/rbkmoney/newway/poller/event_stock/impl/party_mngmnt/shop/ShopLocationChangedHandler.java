package com.rbkmoney.newway.poller.event_stock.impl.party_mngmnt.shop;

import com.rbkmoney.damsel.domain.ShopLocation;
import com.rbkmoney.damsel.payment_processing.PartyChange;
import com.rbkmoney.damsel.payment_processing.ShopEffectUnit;
import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.machinegun.eventsink.MachineEvent;
import com.rbkmoney.newway.dao.party.iface.ShopDao;
import com.rbkmoney.newway.domain.tables.pojos.Shop;
import com.rbkmoney.newway.exception.NotFoundException;
import com.rbkmoney.newway.poller.event_stock.impl.party_mngmnt.AbstractClaimChangedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopLocationChangedHandler extends AbstractClaimChangedHandler {

    private final ShopDao shopDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(PartyChange change, MachineEvent event) {
        long eventId = event.getEventId();
        getClaimStatus(change).getAccepted().getEffects().stream()
                .filter(e -> e.isSetShopEffect() && e.getShopEffect().getEffect().isSetLocationChanged()).forEach(e -> {
            ShopEffectUnit shopEffect = e.getShopEffect();
            ShopLocation locationChanged = shopEffect.getEffect().getLocationChanged();
            String shopId = shopEffect.getShopId();
            String partyId = event.getSourceId();
            log.info("Start shop locationChanged handling, eventId={}, partyId={}, shopId={}", eventId, partyId, shopId);
            Shop shopSource = shopDao.get(partyId, shopId);
            if (shopSource == null) {
                throw new NotFoundException(String.format("Shop not found, shopId='%s'", shopId));
            }
            shopSource.setId(null);
            shopSource.setRevision(null);
            shopSource.setWtime(null);
            shopSource.setEventId(eventId);
            shopSource.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
            if (locationChanged.isSetUrl()) {
                shopSource.setLocationUrl(locationChanged.getUrl());
            } else {
                throw new IllegalArgumentException("Illegal shop location " + locationChanged);
            }
            shopDao.updateNotCurrent(partyId, shopId);
            shopDao.save(shopSource);
            log.info("Shop locationChanged has been saved, eventId={}, partyId={}, shopId={}", eventId, partyId, shopId);
        });
    }
}

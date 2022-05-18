package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.Change;
import dev.vality.fistful.withdrawal.Route;
import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.withdrawal.iface.FistfulCashFlowDao;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalDao;
import dev.vality.newway.domain.enums.FistfulCashFlowChangeType;
import dev.vality.newway.domain.tables.pojos.FistfulCashFlow;
import dev.vality.newway.domain.tables.pojos.Withdrawal;
import dev.vality.newway.factory.machine.event.MachineEventCopyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalRouteChangeHandler implements WithdrawalHandler {

    private final WithdrawalDao withdrawalDao;
    private final FistfulCashFlowDao fistfulCashFlowDao;
    private final MachineEventCopyFactory<Withdrawal, String> machineEventCopyFactory;

    @Getter
    private final Filter filter =
            new PathConditionFilter(new PathConditionRule("change.route.route", new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Change change = timestampedChange.getChange();
        long sequenceId = event.getEventId();
        String withdrawalId = event.getSourceId();
        log.info("Start withdrawal provider id changed handling, sequenceId={}, withdrawalId={}", sequenceId,
                withdrawalId);

        final Withdrawal withdrawalOld = withdrawalDao.get(withdrawalId);
        Withdrawal withdrawalNew = machineEventCopyFactory
                .create(event, sequenceId, withdrawalId, withdrawalOld, timestampedChange.getOccuredAt());

        Route route = change.getRoute().getRoute();
        int providerId = route.getProviderId();
        String providerIdLegacy = route.getProviderIdLegacy();
        withdrawalNew.setProviderId(providerId);
        withdrawalNew.setProviderIdLegacy(providerIdLegacy);

        withdrawalDao.save(withdrawalNew).ifPresentOrElse(
                id -> {
                    withdrawalDao.updateNotCurrent(withdrawalOld.getId());
                    List<FistfulCashFlow> cashFlows =
                            fistfulCashFlowDao.getByObjId(withdrawalOld.getId(), FistfulCashFlowChangeType.withdrawal);
                    cashFlows.forEach(pcf -> {
                        pcf.setId(null);
                        pcf.setObjId(id);
                    });
                    fistfulCashFlowDao.save(cashFlows);
                    log.info("Withdrawal provider id have been changed, sequenceId={}, withdrawalId={}", sequenceId,
                            withdrawalId);
                },
                () -> log.info("Withdrawal provider id have been changed, sequenceId={}, withdrawalId={}", sequenceId,
                        withdrawalId));
    }

}

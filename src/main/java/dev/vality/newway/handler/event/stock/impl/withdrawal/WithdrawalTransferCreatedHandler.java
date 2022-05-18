package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.cashflow.FinalCashFlowPosting;
import dev.vality.fistful.withdrawal.Change;
import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.withdrawal.iface.FistfulCashFlowDao;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalDao;
import dev.vality.newway.domain.enums.FistfulCashFlowChangeType;
import dev.vality.newway.domain.enums.WithdrawalTransferStatus;
import dev.vality.newway.domain.tables.pojos.FistfulCashFlow;
import dev.vality.newway.domain.tables.pojos.Withdrawal;
import dev.vality.newway.factory.machine.event.MachineEventCopyFactory;
import dev.vality.newway.util.FistfulCashFlowUtil;
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
public class WithdrawalTransferCreatedHandler implements WithdrawalHandler {

    private final WithdrawalDao withdrawalDao;
    private final FistfulCashFlowDao fistfulCashFlowDao;
    private final MachineEventCopyFactory<Withdrawal, String> machineEventCopyFactory;

    @Getter
    private final Filter filter = new PathConditionFilter(
            new PathConditionRule("change.transfer.payload.created.transfer.cashflow", new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Change change = timestampedChange.getChange();
        long sequenceId = event.getEventId();
        String withdrawalId = event.getSourceId();
        log.info("Start withdrawal transfer created handling, sequenceId={}, withdrawalId={}, transfer={}",
                sequenceId, withdrawalId, change.getTransfer());

        final Withdrawal withdrawalOld = withdrawalDao.get(withdrawalId);
        Withdrawal withdrawalNew = machineEventCopyFactory
                .create(event, sequenceId, withdrawalId, withdrawalOld, timestampedChange.getOccuredAt());

        withdrawalNew.setWithdrawalTransferStatus(WithdrawalTransferStatus.created);
        List<FinalCashFlowPosting> postings =
                change.getTransfer().getPayload().getCreated().getTransfer().getCashflow().getPostings();
        withdrawalNew.setFee(FistfulCashFlowUtil.getFistfulFee(postings));
        withdrawalNew.setProviderFee(FistfulCashFlowUtil.getFistfulProviderFee(postings));

        withdrawalDao.save(withdrawalNew).ifPresentOrElse(
                id -> {
                    withdrawalDao.updateNotCurrent(withdrawalOld.getId());
                    List<FistfulCashFlow> fistfulCashFlows = FistfulCashFlowUtil
                            .convertFistfulCashFlows(postings, id, FistfulCashFlowChangeType.withdrawal);
                    fistfulCashFlowDao.save(fistfulCashFlows);
                    log.info("Withdrawal transfer have been changed, sequenceId={}, withdrawalId={}", sequenceId,
                            withdrawalId);
                },
                () -> log.info("Withdrawal transfer have been changed, sequenceId={}, withdrawalId={}", sequenceId,
                        withdrawalId));
    }

}

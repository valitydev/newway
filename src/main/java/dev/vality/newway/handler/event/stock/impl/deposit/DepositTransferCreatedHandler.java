package dev.vality.newway.handler.event.stock.impl.deposit;

import dev.vality.fistful.cashflow.FinalCashFlowPosting;
import dev.vality.fistful.deposit.Change;
import dev.vality.fistful.deposit.TimestampedChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.deposit.iface.DepositDao;
import dev.vality.newway.dao.withdrawal.iface.FistfulCashFlowDao;
import dev.vality.newway.domain.enums.DepositTransferStatus;
import dev.vality.newway.domain.enums.FistfulCashFlowChangeType;
import dev.vality.newway.domain.tables.pojos.Deposit;
import dev.vality.newway.domain.tables.pojos.FistfulCashFlow;
import dev.vality.newway.factory.MachineEventCopyFactory;
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
public class DepositTransferCreatedHandler implements DepositHandler {

    private final DepositDao depositDao;
    private final FistfulCashFlowDao fistfulCashFlowDao;
    private final MachineEventCopyFactory<Deposit, String> depositMachineEventCopyFactory;

    @Getter
    private final Filter filter = new PathConditionFilter(
            new PathConditionRule("change.transfer.payload.created.transfer.cashflow", new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Change change = timestampedChange.getChange();
        long sequenceId = event.getEventId();
        String depositId = event.getSourceId();
        log.info("Start deposit transfer created handling, sequenceId={}, depositId={}", sequenceId, depositId);
        final Deposit depositOld = depositDao.get(depositId);
        List<FinalCashFlowPosting> postings =
                change.getTransfer().getPayload().getCreated().getTransfer().getCashflow().getPostings();
        Deposit depositNew = depositMachineEventCopyFactory
                .create(event, sequenceId, depositId, depositOld, timestampedChange.getOccuredAt());
        depositNew.setDepositTransferStatus(DepositTransferStatus.created);
        depositNew.setFee(FistfulCashFlowUtil.getFistfulFee(postings));
        depositNew.setProviderFee(FistfulCashFlowUtil.getFistfulProviderFee(postings));

        depositDao.save(depositNew).ifPresentOrElse(
                id -> {
                    depositDao.updateNotCurrent(depositOld.getId());
                    List<FistfulCashFlow> fistfulCashFlows = FistfulCashFlowUtil
                            .convertFistfulCashFlows(postings, id, FistfulCashFlowChangeType.deposit);
                    fistfulCashFlowDao.save(fistfulCashFlows);
                },
                () -> log.info("Deposit transfer have been saved, sequenceId={}, depositId={}", sequenceId, depositId)
        );
    }

}

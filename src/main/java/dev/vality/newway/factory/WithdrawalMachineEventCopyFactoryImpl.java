package dev.vality.newway.factory;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.Withdrawal;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalMachineEventCopyFactoryImpl implements MachineEventCopyFactory<Withdrawal, String> {

    @Override
    public Withdrawal create(MachineEvent event, Long sequenceId, String withdrawalId, Withdrawal withdrawalOld,
                             String occurredAt) {
        Withdrawal withdrawal = null;
        if (withdrawalOld != null) {
            withdrawal = new Withdrawal(withdrawalOld);
        } else {
            withdrawal = new Withdrawal();
        }
        withdrawal.setId(null);
        withdrawal.setWtime(null);
        withdrawal.setSequenceId(sequenceId.intValue());
        withdrawal.setWithdrawalId(withdrawalId);
        withdrawal.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        withdrawal.setEventOccuredAt(TypeUtil.stringToLocalDateTime(occurredAt));
        return withdrawal;
    }

    @Override
    public Withdrawal create(MachineEvent event, Long sequenceId, String id, String occurredAt) {
        return create(event, sequenceId, id, null, occurredAt);
    }

}

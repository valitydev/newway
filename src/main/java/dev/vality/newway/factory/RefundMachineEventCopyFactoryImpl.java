package dev.vality.newway.factory;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.Refund;
import org.springframework.stereotype.Component;

@Component
public class RefundMachineEventCopyFactoryImpl implements MachineEventCopyFactory<Refund, Integer> {

    @Override
    public Refund create(MachineEvent event, Long sequenceId, Integer changeId, Refund old, String occurredAt) {
        Refund refund = null;
        if (old != null) {
            refund = new Refund(old);
        } else {
            refund = new Refund();
        }
        refund.setId(null);
        refund.setWtime(null);
        refund.setChangeId(changeId);
        refund.setSequenceId(sequenceId);
        refund.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getCreatedAt()));
        return refund;
    }

    @Override
    public Refund create(MachineEvent event, Long sequenceId, Integer id, String occurredAt) {
        return create(event, sequenceId, id, null, occurredAt);
    }

}

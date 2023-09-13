package dev.vality.newway.service;

import dev.vality.newway.handler.event.stock.impl.withdrawal.WithdrawalAdjustmentHandler;
import dev.vality.newway.handler.event.stock.impl.withdrawal.WithdrawalHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalHandlerFilterService {

    @Value("${kafka.topics.withdrawal-adjustment.enabled}")
    private boolean withdrawalAdjustmentListenerEnabled;

    private final List<WithdrawalAdjustmentHandler> withdrawalAdjustmentHandlers;

    public List<WithdrawalHandler> filterAdjustment(List<WithdrawalHandler> handlers) {
        if (withdrawalAdjustmentListenerEnabled) {
            List<WithdrawalHandler> filteredHandlerList = new ArrayList<>(handlers);
            List<WithdrawalHandler> excludeHandlers = withdrawalAdjustmentHandlers.stream()
                    .map(WithdrawalHandler.class::cast)
                    .toList();
            filteredHandlerList.removeAll(excludeHandlers);
            log.debug("Exclude adjustment handlers");
            return filteredHandlerList;
        } else {
            return handlers;
        }
    }
}

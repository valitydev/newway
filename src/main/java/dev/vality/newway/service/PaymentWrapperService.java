package dev.vality.newway.service;

import com.github.benmanes.caffeine.cache.Cache;
import dev.vality.newway.dao.invoicing.iface.CashFlowDao;
import dev.vality.newway.dao.invoicing.iface.PaymentDao;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWrapperService {

    private final PaymentDao paymentDao;
    private final CashFlowDao cashFlowDao;
    private final Cache<InvoicingKey, PaymentWrapper> paymentDataCache;

    public PaymentWrapper get(String invoiceId, String paymentId,
                              long sequenceId, Integer changeId,
                              LocalStorage storage) throws DaoException, NotFoundException {
        InvoicingKey key = InvoicingKey.buildKey(invoiceId, paymentId);
        PaymentWrapper paymentWrapper = (PaymentWrapper) storage.get(key);
        if (paymentWrapper != null) {
            paymentWrapper = paymentWrapper.copy();
        } else {
            paymentWrapper = paymentDataCache.getIfPresent(key);
            if (paymentWrapper != null) {
                paymentWrapper = paymentWrapper.copy();
            } else {
                Payment payment = paymentDao.get(invoiceId, paymentId);
                List<CashFlow> cashFlows = cashFlowDao.getByObjId(payment.getId(), PaymentChangeType.payment);
                paymentWrapper = new PaymentWrapper();
                paymentWrapper.setPayment(payment);
                paymentWrapper.setCashFlows(cashFlows);
                paymentWrapper.setKey(key);
            }
        }
        if ((paymentWrapper.getPayment().getSequenceId() > sequenceId)
                || (paymentWrapper.getPayment().getSequenceId() == sequenceId
                && paymentWrapper.getPayment().getChangeId() >= changeId)) {
            paymentWrapper = null;
        }
        return paymentWrapper;
    }

    public void save(List<PaymentWrapper> paymentWrappers) {
        paymentWrappers.forEach(pw -> paymentDataCache.put(pw.getKey(), pw));
        List<Payment> paymentsForInsert = paymentWrappers.stream()
                .filter(PaymentWrapper::isShouldInsert)
                .map(PaymentWrapper::getPayment)
                .collect(Collectors.toList());
        List<Payment> paymentsForUpdate = paymentWrappers.stream()
                .filter(pw -> !pw.isShouldInsert())
                .map(PaymentWrapper::getPayment)
                .collect(Collectors.toList());
        List<CashFlow> cashFlows = paymentWrappers
                .stream()
                .filter(PaymentWrapper::isShouldInsert)
                .filter(p -> p.getCashFlows() != null)
                .map(PaymentWrapper::getCashFlows)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(paymentsForUpdate)) {
            log.info("Payments for update: {}", paymentsForUpdate.size());
            paymentDao.updateBatch(paymentsForUpdate);
        }
        if (!CollectionUtils.isEmpty(paymentsForInsert)) {
            log.info("Payments for insert: {}", paymentsForInsert.size());
            paymentDao.saveBatch(paymentsForInsert);
        }
        if (!CollectionUtils.isEmpty(cashFlows)) {
            cashFlowDao.save(cashFlows);
        }
    }

    public void switchCurrent(Collection<InvoicingKey> switchIds) {
        paymentDao.switchCurrent(switchIds);
    }
}

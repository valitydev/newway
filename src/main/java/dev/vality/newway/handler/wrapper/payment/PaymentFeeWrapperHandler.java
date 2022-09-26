package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentFeeDao;
import dev.vality.newway.domain.tables.pojos.PaymentFee;
import dev.vality.newway.handler.wrapper.WrapperHandler;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.util.PaymentWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PaymentFeeWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentFeeDao paymentFeeDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentFee)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentWrapper> processableWrappers = wrappers.stream()
                .filter(paymentWrapper -> Objects.nonNull(paymentWrapper.getPaymentFee()))
                .collect(Collectors.toList());
        List<PaymentFee> paymentFees = processableWrappers.stream()
                .map(PaymentWrapper::getPaymentFee)
                .collect(Collectors.toList());
        paymentFeeDao.saveBatch(paymentFees);
        paymentFeeDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(processableWrappers));
    }
}

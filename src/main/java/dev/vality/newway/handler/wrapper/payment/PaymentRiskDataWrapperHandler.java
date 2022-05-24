package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentRiskDataDao;
import dev.vality.newway.domain.tables.pojos.PaymentRiskData;
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
public class PaymentRiskDataWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentRiskDataDao paymentRiskDataDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentRiskData)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentRiskData> paymentRiskDataList = wrappers.stream()
                .map(PaymentWrapper::getPaymentRiskData)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            paymentRiskDataDao.saveBatch(paymentRiskDataList);
            paymentRiskDataDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(wrappers));
    }
}

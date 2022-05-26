package dev.vality.newway.handler.wrapper.payment;

import dev.vality.newway.dao.invoicing.iface.PaymentRouteDao;
import dev.vality.newway.domain.tables.pojos.PaymentRoute;
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
public class PaymentRouteWrapperHandler implements WrapperHandler<PaymentWrapper> {

    private final PaymentRouteDao paymentRouteDao;

    @Override
    public boolean accept(List<PaymentWrapper> wrappers) {
        return wrappers.stream()
                .map(PaymentWrapper::getPaymentRoute)
                .anyMatch(Objects::nonNull);
    }

    @Override
    public void saveBatch(List<PaymentWrapper> wrappers) {
        List<PaymentRoute> paymentRoutes = wrappers.stream()
                .map(PaymentWrapper::getPaymentRoute)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        paymentRouteDao.saveBatch(paymentRoutes);
        paymentRouteDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(wrappers));
    }
}

package dev.vality.newway.util;

import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.model.InvoiceWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceWrapperUtil {

    public static String getInvoiceId(InvoiceWrapper wrapper) {
        if (wrapper.getInvoice() != null && StringUtils.hasLength(wrapper.getInvoice().getInvoiceId())) {
            return wrapper.getInvoice().getInvoiceId();
        } else if (wrapper.getInvoiceStatusInfo() != null
                && StringUtils.hasLength(wrapper.getInvoiceStatusInfo().getInvoiceId())) {
            return wrapper.getInvoiceStatusInfo().getInvoiceId();
        } else if (!CollectionUtils.isEmpty(wrapper.getCarts())) {
            Optional<String> invoiceId = wrapper.getCarts().stream()
                    .map(InvoiceCart::getInvoiceId)
                    .filter(Objects::nonNull)
                    .findFirst();
            if (invoiceId.isPresent()) {
                return invoiceId.get();
            }
        }

        throw new IllegalStateException("No invoice id for wrapper : " + wrapper);
    }

}

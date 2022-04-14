package dev.vality.newway.model;

import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceWrapper {
    private Invoice invoice;
    private InvoiceStatusInfo invoiceStatusInfo;
    private List<InvoiceCart> carts;

    // TODO: не лучше ли написать мапперы или переопределить clone?
    //  Через рефлексию тут медленнее работает, по сути гоняем одинаковые сущности

    public InvoiceWrapper copy() {
        Invoice invoiceTarget = new Invoice();
        BeanUtils.copyProperties(invoice, invoiceTarget);
        InvoiceWrapper invoiceWrapperTarget = new InvoiceWrapper();
        invoiceWrapperTarget.setInvoice(invoiceTarget);
        InvoiceStatusInfo invoiceStatusInfoTarget = new InvoiceStatusInfo();
        BeanUtils.copyProperties(invoiceStatusInfo, invoiceStatusInfoTarget);
        invoiceWrapperTarget.setInvoiceStatusInfo(invoiceStatusInfo);
        if (carts != null) {
            List<InvoiceCart> cartsTarget = new ArrayList<>();
            carts.forEach(c -> {
                InvoiceCart cartTarget = new InvoiceCart();
                BeanUtils.copyProperties(c, cartTarget);
                cartsTarget.add(cartTarget);
            });
            invoiceWrapperTarget.setCarts(cartsTarget);
        }
        return invoiceWrapperTarget;
    }
}

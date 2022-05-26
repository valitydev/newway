package dev.vality.newway.model;

import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.domain.tables.pojos.InvoiceCart;
import dev.vality.newway.domain.tables.pojos.InvoiceStatusInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceWrapper {
    private Invoice invoice;
    private InvoiceStatusInfo invoiceStatusInfo;
    private List<InvoiceCart> carts;
}

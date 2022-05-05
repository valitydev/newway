package dev.vality.newway.dao.invoicing.impl;

import dev.vality.dao.impl.AbstractGenericDao;
import dev.vality.newway.dao.invoicing.iface.CashFlowLinkDao;
import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.model.InvoicingKey;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.beans.Transient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.vality.newway.domain.Tables.CASH_FLOW_LINK;

@Component
public class CashFlowLinkDaoImpl extends AbstractGenericDao implements CashFlowLinkDao {

    public CashFlowLinkDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Transient
    @Override
    public void saveBatch(List<CashFlowLink> links) throws DaoException {
        batchExecute(links.stream()
                .map(status -> getDslContext().newRecord(CASH_FLOW_LINK, status))
                .map(invoiceStatusInfoRecord -> getDslContext().insertInto(CASH_FLOW_LINK)
                        .set(invoiceStatusInfoRecord))
                .collect(Collectors.toList())
        );
    }

    @Override
    public void switchCurrent(Set<InvoicingKey> keys) throws DaoException {
        keys.forEach(key -> {
                    execute(getDslContext().update(CASH_FLOW_LINK)
                            .set(CASH_FLOW_LINK.CURRENT, false)
                            .where(CASH_FLOW_LINK.INVOICE_ID.eq(key.getInvoiceId())
                                    .and(CASH_FLOW_LINK.PAYMENT_ID.eq(key.getPaymentId()))
                                    .and(CASH_FLOW_LINK.CURRENT))
                    );
                    execute(getDslContext().update(CASH_FLOW_LINK)
                            .set(CASH_FLOW_LINK.CURRENT, true)
                            .where(CASH_FLOW_LINK.ID.eq(
                                    DSL.select(DSL.max(CASH_FLOW_LINK.ID))
                                            .from(CASH_FLOW_LINK)
                                            .where(CASH_FLOW_LINK.INVOICE_ID.eq(key.getInvoiceId())
                                                    .and(CASH_FLOW_LINK.PAYMENT_ID.eq(key.getPaymentId())))
                                    ))
                    );
                }
        );
    }
}

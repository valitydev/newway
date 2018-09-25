/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.routines;


import com.rbkmoney.newway.domain.Nw;
import com.rbkmoney.newway.domain.tables.records.CashFlowRecord;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetRefundProviderFeeSfunc2 extends AbstractRoutine<Long> {

    private static final long serialVersionUID = 1243025635;

    /**
     * The parameter <code>nw.get_refund_provider_fee_sfunc.RETURN_VALUE</code>.
     */
    public static final Parameter<Long> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BIGINT, false, false);

    /**
     * The parameter <code>nw.get_refund_provider_fee_sfunc.amount</code>.
     */
    public static final Parameter<Long> AMOUNT = createParameter("amount", org.jooq.impl.SQLDataType.BIGINT, false, false);

    /**
     * The parameter <code>nw.get_refund_provider_fee_sfunc.cash_flow</code>.
     */
    public static final Parameter<CashFlowRecord> CASH_FLOW = createParameter("cash_flow", com.rbkmoney.newway.domain.tables.CashFlow.CASH_FLOW.getDataType(), false, false);

    /**
     * Create a new routine call instance
     */
    public GetRefundProviderFeeSfunc2() {
        super("get_refund_provider_fee_sfunc", Nw.NW, org.jooq.impl.SQLDataType.BIGINT);

        setReturnParameter(RETURN_VALUE);
        addInParameter(AMOUNT);
        addInParameter(CASH_FLOW);
        setOverloaded(true);
    }

    /**
     * Set the <code>amount</code> parameter IN value to the routine
     */
    public void setAmount(Long value) {
        setValue(AMOUNT, value);
    }

    /**
     * Set the <code>amount</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setAmount(Field<Long> field) {
        setField(AMOUNT, field);
    }

    /**
     * Set the <code>cash_flow</code> parameter IN value to the routine
     */
    public void setCashFlow(CashFlowRecord value) {
        setValue(CASH_FLOW, value);
    }

    /**
     * Set the <code>cash_flow</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setCashFlow(Field<CashFlowRecord> field) {
        setField(CASH_FLOW, field);
    }
}

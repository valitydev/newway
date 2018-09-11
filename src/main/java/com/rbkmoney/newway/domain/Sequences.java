/*
 * This file is generated by jOOQ.
 */
package com.rbkmoney.newway.domain;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in nw
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>nw.adjustment_id_seq</code>
     */
    public static final Sequence<Long> ADJUSTMENT_ID_SEQ = new SequenceImpl<Long>("adjustment_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.cash_flow_id_seq</code>
     */
    public static final Sequence<Long> CASH_FLOW_ID_SEQ = new SequenceImpl<Long>("cash_flow_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.category_id_seq</code>
     */
    public static final Sequence<Long> CATEGORY_ID_SEQ = new SequenceImpl<Long>("category_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.contract_adjustment_id_seq</code>
     */
    public static final Sequence<Long> CONTRACT_ADJUSTMENT_ID_SEQ = new SequenceImpl<Long>("contract_adjustment_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.contract_id_seq</code>
     */
    public static final Sequence<Long> CONTRACT_ID_SEQ = new SequenceImpl<Long>("contract_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.contractor_id_seq</code>
     */
    public static final Sequence<Long> CONTRACTOR_ID_SEQ = new SequenceImpl<Long>("contractor_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.invoice_cart_id_seq</code>
     */
    public static final Sequence<Long> INVOICE_CART_ID_SEQ = new SequenceImpl<Long>("invoice_cart_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.invoice_id_seq</code>
     */
    public static final Sequence<Long> INVOICE_ID_SEQ = new SequenceImpl<Long>("invoice_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.party_id_seq</code>
     */
    public static final Sequence<Long> PARTY_ID_SEQ = new SequenceImpl<Long>("party_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.payment_id_seq</code>
     */
    public static final Sequence<Long> PAYMENT_ID_SEQ = new SequenceImpl<Long>("payment_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.payout_id_seq</code>
     */
    public static final Sequence<Long> PAYOUT_ID_SEQ = new SequenceImpl<Long>("payout_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.payout_summary_id_seq</code>
     */
    public static final Sequence<Long> PAYOUT_SUMMARY_ID_SEQ = new SequenceImpl<Long>("payout_summary_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.payout_tool_id_seq</code>
     */
    public static final Sequence<Long> PAYOUT_TOOL_ID_SEQ = new SequenceImpl<Long>("payout_tool_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.refund_id_seq</code>
     */
    public static final Sequence<Long> REFUND_ID_SEQ = new SequenceImpl<Long>("refund_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>nw.shop_id_seq</code>
     */
    public static final Sequence<Long> SHOP_ID_SEQ = new SequenceImpl<Long>("shop_id_seq", Nw.NW, org.jooq.impl.SQLDataType.BIGINT.nullable(false));
}

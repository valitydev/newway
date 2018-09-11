/*
 * This file is generated by jOOQ.
 */
package com.rbkmoney.newway.domain.enums;


import com.rbkmoney.newway.domain.Nw;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum Blocking implements EnumType {

    unblocked("unblocked"),

    blocked("blocked");

    private final String literal;

    private Blocking(String literal) {
        this.literal = literal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Nw.NW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "blocking";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return literal;
    }
}

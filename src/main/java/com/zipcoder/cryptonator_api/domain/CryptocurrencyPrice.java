package com.zipcoder.cryptonator_api.domain;

import java.math.BigDecimal;

public class CryptocurrencyPrice {
    private final String base;
    private final String quote;
    private final BigDecimal amount;

    public CryptocurrencyPrice(String base, String quote, BigDecimal amount) {
        this.base = base;
        this.quote = quote;
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public String getQuote() {
        return quote;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

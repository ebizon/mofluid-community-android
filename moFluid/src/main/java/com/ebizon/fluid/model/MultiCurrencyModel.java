package com.ebizon.fluid.model;

/**
 * Created by avnish on 11/21/17.
 */

public class MultiCurrencyModel {

    private String currency_code;
    private String currency_name;
    private String currency_symbol;

    public MultiCurrencyModel(String currency_code, String currency_name, String currency_symbol) {
        this.currency_code = currency_code;
        this.currency_name = currency_name;
        this.currency_symbol = currency_symbol;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }
}

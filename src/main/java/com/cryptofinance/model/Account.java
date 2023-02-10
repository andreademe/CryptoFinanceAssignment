package com.cryptofinance.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {

    private Long accountId;
    private String name;
    private BigDecimal usdBalance;
    private BigDecimal btcAmount = BigDecimal.ZERO;

    public void subtractFromUsdBalance(BigDecimal priceUsd) {
        setUsdBalance(getUsdBalance().subtract(priceUsd));
    }

    public void addBtcAmount(BigDecimal btcAmount) {
        setBtcAmount(getBtcAmount().add(btcAmount));
    }

    public boolean affords(BigDecimal priceUsd) {
        return this.usdBalance.compareTo(priceUsd) >= 0;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + accountId +
                ", name='" + name + '\'' +
                ", usdBalance=" + usdBalance +
                ", btcAmount=" + btcAmount +
                '}';
    }
}

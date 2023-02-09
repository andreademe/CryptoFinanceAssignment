package com.cryptofinance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
@Getter
public class Account {

    @Setter
    private Long accountId;
    private String name;

    @Setter
    private BigDecimal usdBalance;

    @Setter
    private BigDecimal btcAmount = BigDecimal.ZERO;

    private final List<Order> orders = new ArrayList<>();

    public Account(@JsonProperty("name") String name,
                   @JsonProperty("usd_balance") BigDecimal usdBalance) {
        this.name = name;
        this.usdBalance = usdBalance;
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

    public void addLimitOrder(Order order) {
        this.orders.add(order);
    }

    public void subtractFromUsdBalance(BigDecimal priceUsd) {
        setUsdBalance(getUsdBalance().subtract(priceUsd));
    }

    public void addBtcAmount(BigDecimal btcAmount) {
        setBtcAmount(getBtcAmount().add(btcAmount));
    }

    public boolean canBuy(BigDecimal priceUsd) {
        return this.usdBalance.compareTo(priceUsd) >= 0;
    }
}

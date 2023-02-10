package com.cryptofinance.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class Order {

    @Setter
    private Long orderId;

    private BigDecimal priceLimit;
    private BigDecimal amount;
    private OrderSide orderSide;
    private String tradingSymbol;
    private Account account;

    @Setter
    private boolean executed;

    @Setter
    private BigDecimal executionPrice;

    public Order(BigDecimal priceLimit, BigDecimal amount, OrderSide orderSide, String tradingSymbol, Account account) {
        this.priceLimit = priceLimit;
        this.amount = amount;
        this.orderSide = orderSide;
        this.tradingSymbol = tradingSymbol;
        this.account = account;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", priceLimit=" + priceLimit +
                ", amount=" + amount +
                ", orderSide=" + orderSide +
                ", tradingSymbol='" + tradingSymbol + '\'' +
                ", executed=" + executed +
                '}';
    }
}

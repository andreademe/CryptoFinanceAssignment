package com.cryptofinance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties
@Getter
public class Order {

    @Setter
    private Long orderId;

    private Long accountId;
    private BigDecimal priceLimit;
    private BigDecimal amount;

    private OrderType orderType;
    private OrderSide orderSide;
    private String tradingSymbol;

    @Setter
    private boolean executed;

    public Order(@JsonProperty("account_id") Long accountId,
                 @JsonProperty("price_limit") BigDecimal priceLimit,
                 @JsonProperty("amount") BigDecimal amount) {
        this.accountId = accountId;
        this.priceLimit = priceLimit;
        this.amount = amount;
        this.orderType = OrderType.LIMIT;
        this.orderSide = OrderSide.BUY;
        this.tradingSymbol = "BTC-USD";
    }

    @Override
    public String toString() {
        return "LimitOrder{" +
                "id=" + orderId +
                ", accountId=" + accountId +
                ", priceLimit=" + priceLimit +
                ", amount=" + amount +
                '}';
    }
}

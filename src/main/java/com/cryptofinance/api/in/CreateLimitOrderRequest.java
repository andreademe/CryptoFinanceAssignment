package com.cryptofinance.api.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties
@Getter
public class CreateLimitOrderRequest {
    @Setter
    private Long orderId;

    private Long accountId;
    private BigDecimal priceLimit;
    private BigDecimal amount;

    @Setter
    private boolean executed;

    public CreateLimitOrderRequest(@JsonProperty("account_id") Long accountId,
                                   @JsonProperty("price_limit") BigDecimal priceLimit,
                                   @JsonProperty("amount") BigDecimal amount) {
        this.accountId = accountId;
        this.priceLimit = priceLimit;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CreateLimitOrderRequest{" +
                "orderId=" + orderId +
                ", accountId=" + accountId +
                ", priceLimit=" + priceLimit +
                ", amount=" + amount +
                ", executed=" + executed +
                '}';
    }
}

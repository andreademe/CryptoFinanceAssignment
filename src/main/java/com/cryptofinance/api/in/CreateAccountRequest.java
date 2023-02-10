package com.cryptofinance.api.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateAccountRequest {

    private String name;
    private BigDecimal usdBalance;

    public CreateAccountRequest(@JsonProperty("name") String name,
                                @JsonProperty("usd_balance") BigDecimal usdBalance) {
        this.name = name;
        this.usdBalance = usdBalance;
    }

    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                "name='" + name + '\'' +
                ", usdBalance=" + usdBalance +
                '}';
    }
}

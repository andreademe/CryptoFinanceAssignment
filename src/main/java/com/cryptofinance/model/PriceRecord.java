package com.cryptofinance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@JsonIgnoreProperties
@Getter
public class PriceRecord {

    private BigDecimal price;
    private String timestamp;

    public PriceRecord(@JsonProperty("price") BigDecimal price,
                       @JsonProperty("timestamp") String timestamp) {
        this.price = price;
        this.timestamp = timestamp;
    }
}

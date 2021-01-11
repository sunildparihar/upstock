package com.upstock.trade.commons.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {

    @JsonProperty(required = true, value = "sym")
    private String symbol;

    @JsonProperty(required = true, value = "P")
    private double price;

    @JsonProperty(required = true, value = "Q")
    private double quantity;

    @JsonProperty(required = true, value = "TS2")
    private BigInteger ts;

    private boolean isLastTrade;
    private boolean isEmpty;

}

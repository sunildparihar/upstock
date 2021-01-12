package com.upstock.trade.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSubscription {

    @JsonProperty(required = true, value = "event")
    private String event;

    @JsonProperty(required = true, value = "symbol")
    private String symbol;

    @JsonProperty(required = true, value = "interval")
    private int interval;

}

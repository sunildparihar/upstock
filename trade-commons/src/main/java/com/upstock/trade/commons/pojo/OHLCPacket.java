package com.upstock.trade.commons.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.upstock.trade.commons.constant.TradeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OHLCPacket {

    @JsonProperty(value = "o")
    @JsonSerialize(using = DoubleSerializer.class)
    private Double open;

    @JsonProperty(value = "h")
    @JsonSerialize(using = DoubleSerializer.class)
    private Double high;

    @JsonProperty(value = "l")
    @JsonSerialize(using = DoubleSerializer.class)
    private Double low;

    @JsonProperty(value = "c")
    @JsonSerialize(using = DoubleSerializer.class)
    private Double close;

    @JsonProperty
    @JsonSerialize(using = DoubleSerializer.class)
    private Double volume;

    @JsonProperty
    private String event = TradeConstants.EVENT_NOTIFY;

    @JsonProperty(required = true, value = "symbol")
    private String symbol;

    @JsonProperty(required = true, value = "bar_num")
    private int barNumber;

    @JsonIgnore
    private long barStartTime;

    @JsonIgnore
    private Double lastTradedPrice;

    @JsonIgnore
    private int barSizeInSeconds;

    @JsonIgnore
    private boolean newTradePacket;

    @JsonIgnore
    private boolean lastPacket;

}

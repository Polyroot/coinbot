package com.polyroot.coinbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventType {

    @JsonProperty("aggTrade")
    AGG_TRADE("aggTrade"),
    @JsonProperty("trade")
    TRADE("trade"),
    @JsonProperty("kline")
    KLINE("kline"),
    @JsonProperty("24hrMiniTicker")
    MINI_TICKER_24HR("24hrMiniTicker"),
    @JsonProperty("24hrTicker")
    TICKER_24HR("24hrTicker"),
    @JsonProperty("depthUpdate")
    DEPTH_UPDATE("depthUpdate");

    private String event;

}

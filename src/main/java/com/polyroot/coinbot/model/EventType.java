package com.polyroot.coinbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.polyroot.coinbot.model.dto.AggTradeResponse;
import com.polyroot.coinbot.model.dto.DepthResponse;
import com.polyroot.coinbot.model.dto.TradeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventType {

    @JsonProperty("aggTrade")
    AGG_TRADE("aggTrade", AggTradeResponse.class),
//    @JsonProperty("trade")
//    TRADE("trade"),
//    @JsonProperty("kline")
//    KLINE("kline"),
//    @JsonProperty("24hrMiniTicker")
//    MINI_TICKER_24HR("24hrMiniTicker"),
//    @JsonProperty("24hrTicker")
//    TICKER_24HR("24hrTicker"),
    @JsonProperty("depthUpdate")
    DEPTH_UPDATE("depthUpdate", DepthResponse.class);

    private String event;
    private Class aClass;

}

package com.polyroot.coinbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.dto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventType {

    @JsonProperty("aggTrade")
    AGG_TRADE("aggTrade", AggTradeResponse.class),
    @JsonProperty("trade")
    TRADE("trade", TradeResponse.class),
    @JsonProperty("kline")
    KLINE("kline", KLineResponse.class),
    @JsonProperty("24hrMiniTicker")
    MINI_TICKER("24hrMiniTicker", MiniTickerResponse.class),
    @JsonProperty("24hrTicker")
    TICKER("24hrTicker", TickerResponse.class),
    @JsonProperty("depthUpdate")
    DEPTH("depthUpdate", DepthResponse.class),
    @JsonProperty("bookTicker") //рабочий, но недоделанный
    BOOK_TICKER("bookTicker", BookTickerResponse.class),
    @JsonProperty("markPriceUpdate")
    MARK_PRICE("markPriceUpdate", MarkPriceResponse.class), //не рабочий
    @JsonProperty("forceOrder")
    FORCE_ORDER("forceOrder", ForceOrderResponse.class);

    private String event;
    private Class aClass;

}

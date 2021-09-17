package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MiniTickerResponse {

    @JsonProperty("e")
    private EventType eventType;
    @JsonProperty("E")
    private ZonedDateTime eventTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("o")
    private Float openPrice;
    @JsonProperty("c")
    private Float closePrice;
    @JsonProperty("h")
    private Float highPrice;
    @JsonProperty("l")
    private Float lowPrice;
    @JsonProperty("v")
    private Float totalTradedBaseAssetVolume;
    @JsonProperty("q")
    private Float totalTradedQuoteAssetVolume;

}

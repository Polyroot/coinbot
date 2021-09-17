package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class TickerResponse {

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
    @JsonProperty("p")
    private Float changePrice;
    @JsonProperty("P")
    private Float changePercentPrice;
    @JsonProperty("w")
    private Float weightedAveragePrice;
    @JsonProperty("Q")
    private Float lastQuantity;
    @JsonProperty("O")
    private ZonedDateTime statisticsOpenTime;
    @JsonProperty("C")
    private ZonedDateTime statisticsCloseTime;
    @JsonProperty("F")
    private Long firstTradeID;
    @JsonProperty("L")
    private Long lastTradeID;
    @JsonProperty("n")
    private Integer totalNumberOfTrades;

}

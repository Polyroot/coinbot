package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class CandlestickResponse {

        @JsonProperty("t")
        private ZonedDateTime startTime;
        @JsonProperty("T")
        private ZonedDateTime stopTime;
        @JsonProperty("s")
        private String symbol;
        @JsonProperty("i")
        private String interval;
        @JsonProperty("f")
        private Long firstTradeID;
        @JsonProperty("L")
        private Long lastTradeID;
        @JsonProperty("o")
        private Float openPrice;
        @JsonProperty("c")
        private Float closePrice;
        @JsonProperty("h")
        private Float highPrice;
        @JsonProperty("l")
        private Float lowPrice;
        @JsonProperty("v")
        private Float baseAssetVolume;
        @JsonProperty("n")
        private Integer numberOfTrades;
        @JsonProperty("x")
        private Boolean isThisKlineClosed;
        @JsonProperty("q")
        private Float quoteAssetVolume;
        @JsonProperty("V")
        private Float takerBuyBaseAssetVolume;
        @JsonProperty("Q")
        private Float takerBuyQuoteAssetVolume;
        @JsonProperty("B")
        private String ignore;

}

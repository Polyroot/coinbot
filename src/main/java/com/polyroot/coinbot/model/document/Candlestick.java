package com.polyroot.coinbot.model.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class Candlestick {

        private LocalDateTime startTime;
        private LocalDateTime stopTime;
        private String symbol;
        private String interval;
        private Long firstTradeID;
        private Long lastTradeID;
        private Float openPrice;
        private Float closePrice;
        private Float highPrice;
        private Float lowPrice;
        private Float baseAssetVolume;
        private Integer numberOfTrades;
        private Boolean isThisKlineClosed;
        private Float quoteAssetVolume;
        private Float takerBuyBaseAssetVolume;
        private Float takerBuyQuoteAssetVolume;
        private String ignore;

}

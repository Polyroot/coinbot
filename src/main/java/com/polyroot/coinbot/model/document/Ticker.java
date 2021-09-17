package com.polyroot.coinbot.model.document;

import com.polyroot.coinbot.model.EventType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@ToString
@Getter@Setter
public class Ticker {

    @Id
    private String id;
    private EventType eventType;
    private LocalDateTime eventTime;
    private String symbol;
    private Float openPrice;
    private Float closePrice;
    private Float highPrice;
    private Float lowPrice;
    private Float totalTradedBaseAssetVolume;
    private Float totalTradedQuoteAssetVolume;
    private Float changePrice;
    private Float changePercentPrice;
    private Float weightedAveragePrice;
    private Float lastQuantity;
    private LocalDateTime statisticsOpenTime;
    private LocalDateTime statisticsCloseTime;
    private Long firstTradeID;
    private Long lastTradeID;
    private Integer totalNumberOfTrades;

}

package com.polyroot.coinbot.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MiniTicker {

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

}

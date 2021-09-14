package com.polyroot.coinbot.model.document;

import com.polyroot.coinbot.model.EventType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@ToString
@Getter@Setter
public class AggTrade {

    @Id
    private String id;
    private EventType eventType;
    private Instant eventTime;
    private String symbol;
    private Long aggTradeId;
    private Float price;
    private Float quantity;
    private Long firstTradeID;
    private Long lastTradeID;
    private Instant tradeTime;
    private Boolean buyer;

}

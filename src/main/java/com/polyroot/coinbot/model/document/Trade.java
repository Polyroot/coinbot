package com.polyroot.coinbot.model.document;

import com.polyroot.coinbot.model.EventType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@ToString
@Getter@Setter
public class Trade {

    @Id
    private String id;
    private EventType eventType;
    private LocalDateTime eventTime;
    private String symbol;
    private LocalDateTime time;
    private Float price;
    private Float quantity;
    private Long firstTradeID;
    private Long lastTradeID;
    private LocalDateTime tradeTime;
    private Boolean buyer;
    private Boolean buyerM;

}

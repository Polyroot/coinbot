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
public class BookTicker {

      @Id
      private String id;
      private EventType eventType;
      private LocalDateTime eventTime;
      private String symbol;
      private Float bestBidPrice;
      private Float bestBidQty;
      private Float bestAskPrice;
      private Float bestAskQty;
      private Integer orderBookUpdateId;
      private LocalDateTime transactionTime;

}

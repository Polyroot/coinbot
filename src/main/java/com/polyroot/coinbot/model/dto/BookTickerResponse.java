package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class BookTickerResponse {

      @JsonProperty("e")
      private EventType eventType;
      @JsonProperty("E")
      private ZonedDateTime eventTime;
      @JsonProperty("s")
      private String symbol;
      @JsonProperty("b")
      private Float bestBidPrice;
      @JsonProperty("B")
      private Float bestBidQty;
      @JsonProperty("a")
      private Float bestAskPrice;
      @JsonProperty("A")
      private Float bestAskQty;
      @JsonProperty("u")
      private Integer orderBookUpdateId;
      @JsonProperty("T")
      private ZonedDateTime transactionTime;

}

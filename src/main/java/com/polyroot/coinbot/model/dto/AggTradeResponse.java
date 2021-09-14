package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class AggTradeResponse {

    @JsonProperty("e")
    private EventType eventType;
    @JsonProperty("E")
    private Instant eventTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("a")
    private Long aggTradeId;
    @JsonProperty("p")
    private Float price;
    @JsonProperty("q")
    private Float quantity;
    @JsonProperty("f")
    private Long firstTradeID;
    @JsonProperty("l")
    private Long lastTradeID;
    @JsonProperty("T")
    private Instant tradeTime;
    @JsonProperty("m")
    private Boolean buyer;

}

package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class KLineResponse {

    @JsonProperty("e")
    private EventType eventType;
    @JsonProperty("E")
    private ZonedDateTime eventTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("k")
    private CandlestickResponse candlestickResponse;

}

package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ForceOrderResponse {

    @JsonProperty("e")
    private EventType eventType;
    @JsonProperty("E")
    private ZonedDateTime eventTime;
    @JsonProperty("o")
    private OrderResponse orderResponse;

}

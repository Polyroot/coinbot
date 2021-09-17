package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter@Setter
public class DepthResponse {

    @JsonProperty("e")
    private EventType eventType;
    @JsonProperty("E")
    private ZonedDateTime eventTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("U")
    private Long eventFirstUpdateId;
    @JsonProperty("u")
    private Long eventFinalUpdateId;
    @JsonProperty("bids")
    @JsonAlias("b")
    private List<List<Float>> bids;
    @JsonProperty("asks")
    @JsonAlias("a")
    private List<List<Float>> asks;

}

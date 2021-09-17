package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderResponse {

    @JsonProperty("s")
    private String symbol;
    @JsonProperty("S")
    private String side;
    @JsonProperty("o")
    private String orderType;
    @JsonProperty("f")
    private String timeInForce;
    @JsonProperty("q")
    private Float originalQuantity;
    @JsonProperty("p")
    private Float price;
    @JsonProperty("ap")
    private Float averagePrice;
    @JsonProperty("X")
    private String orderStatus;
    @JsonProperty("l")
    private Float orderLastFilledQuantity;
    @JsonProperty("z")
    private Float orderFilledAccumulatedQuantity;
    @JsonProperty("T")
    private ZonedDateTime orderTradeTime;

}

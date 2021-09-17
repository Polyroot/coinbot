package com.polyroot.coinbot.model.document;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class Order {

    private String symbol;
    private String side;
    private String orderType;
    private String timeInForce;
    private Float originalQuantity;
    private Float price;
    private Float averagePrice;
    private String orderStatus;
    private Float orderLastFilledQuantity;
    private Float orderFilledAccumulatedQuantity;
    private LocalDateTime orderTradeTime;

}

package com.polyroot.coinbot.log;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogMessage {
    private String module;
    private String rqId;
    private Integer statusCode;
    private String statusDesc;
    private String timestamp;
    private String event;

}

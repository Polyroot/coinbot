package com.polyroot.coinbot.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@AllArgsConstructor
public enum LogHelper {

    ERROR_TIMEOUT(-2, "Catch timeout"),
    CANNOT_FORWARD_TO_FLUX(-100, "cannot forward to flux");

    private final int statusCode;
    private final String statusDesc;

    public LogMessage getLogMessage(String rqId, String moduleName, String event) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        return LogMessage.builder()
                .rqId(rqId)
                .statusCode(statusCode)
                .statusDesc(statusDesc)
                .module(moduleName)
                .event(event.length() > 300 ? event.substring(0, 300) + "..." : event)
                .timestamp(ts.toString())
                .build();
    }

}

package com.polyroot.coinbot.model.document;

import com.polyroot.coinbot.model.EventType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@ToString
@Getter@Setter
public class Depth {

    @Id
    private String id;
    private EventType eventType;
    private Instant eventTime;
    private String symbol;
    private Long eventFirstUpdateId;
    private Long eventFinalUpdateId;
    private List<List<Float>> bids;
    private List<List<Float>> asks;

}

package com.polyroot.coinbot.model.document;

import com.polyroot.coinbot.model.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public class ForceOrder {

    @Id
    private String id;
    private EventType eventType;
    private LocalDateTime eventTime;
    private Order orderResponse;

}

package com.polyroot.coinbot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;

@Data
@NoArgsConstructor
public class IncomingRequestEnvelope {

    private String rqId;
    private ServerRequest request;

    public IncomingRequestEnvelope(String requestId, ServerRequest request) {
        this.rqId = requestId;
        this.request = request;
    }

}

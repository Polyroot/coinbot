package com.polyroot.coinbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutgoingResponseEnvelope {

    private String rqId;
    private Mono<ServerResponse> response;

}

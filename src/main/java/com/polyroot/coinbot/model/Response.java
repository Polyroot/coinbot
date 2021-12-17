package com.polyroot.coinbot.model;

import com.polyroot.coinbot.model.dto.TestResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Getter
public enum Response {

    OK(200, new TestResponse("success", "Yeeeea")),
    ERROR(400, new TestResponse("error", "Sukaaa"));

    private int responseCode;
    private TestResponse responseBody;

    public Mono<ServerResponse> monoServerResponse() {

        return ServerResponse
                .status(responseCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(responseBody), TestResponse.class);
    }
}

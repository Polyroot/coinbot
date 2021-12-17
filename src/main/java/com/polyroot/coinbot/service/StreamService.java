package com.polyroot.coinbot.service;

import com.polyroot.coinbot.model.dto.IncomingRequestEnvelope;
import com.polyroot.coinbot.model.dto.TestDto;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import com.polyroot.coinbot.streaming.manage.MonoAdaptersManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.polyroot.coinbot.model.Response.ERROR;
import static com.polyroot.coinbot.model.Response.OK;

@Service
@Slf4j
public class StreamService {

    @Autowired
    private FluxAdaptersManager<IncomingRequestEnvelope> fluxManager;

    @Autowired
    private MonoAdaptersManager<ServerResponse> monoManager;

    @Bean(name="configMainLogicNode")
    public void configMainFluxChain() {

        fluxManager.getStream("test")
                .log("----------")
                .flatMap(incomingRequestEnvelope ->
                        incomingRequestEnvelope.getRequest()
                                .bodyToMono(TestDto.class)
                                .map(testDto -> Integer.parseInt(testDto.getOne()))
                                .doOnNext(testDto -> OK.monoServerResponse()
                                        .doOnNext(monoManager.getInput(incomingRequestEnvelope.getRqId()))
                                        .subscribe())
                                .onErrorContinue((throwable, o) -> ERROR.monoServerResponse()
                                        .doOnNext(monoManager.getInput(incomingRequestEnvelope.getRqId()))
                                        .subscribe())
                )
                .subscribe();
    }
}

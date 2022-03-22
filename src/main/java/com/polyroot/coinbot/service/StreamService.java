package com.polyroot.coinbot.service;

import com.polyroot.coinbot.model.dto.FluxRoutedEnvelope;
import com.polyroot.coinbot.model.dto.IncomingRequestEnvelope;
import com.polyroot.coinbot.model.dto.TestDto;
import com.polyroot.coinbot.streaming.RequestResponseAdapter;
import com.polyroot.coinbot.streaming.manage.FluxAdaptersManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
public class StreamService {

    @Autowired
    private FluxAdaptersManager<IncomingRequestEnvelope> fluxManagerRequest;

    @Autowired
    private FluxAdaptersManager<FluxRoutedEnvelope> fluxManagerRoutedEnvelope;

    @Autowired
    private RequestResponseAdapter requestResponseAdapter;

    @Bean(name="configMainLogicNode")
    public void configMainFluxChain() {

        fluxManagerRequest.getStream("test")
                .log("1111111")
                .flatMap(incomingRequestEnvelope ->
                        incomingRequestEnvelope.getRequest()
                                .bodyToMono(TestDto.class)
                                .map(testDto -> FluxRoutedEnvelope.builder()
                                        .key(incomingRequestEnvelope.getRqId())
                                        .value(testDto)
                                        .routeToAdapter("test-logic")
                                        .build())
                )
                .doOnNext(requestResponseAdapter.routeEnvelopeToFlux())
                .subscribe();

        fluxManagerRoutedEnvelope.getStream("test-logic")
                .log("222222")
                .flatMap(fluxRoutedEnvelope ->
                        Mono.just(fluxRoutedEnvelope.getValue())
                                .cast(TestDto.class)
                                .flatMap(testDto -> Mono.fromCallable(() -> foo(testDto)))
                                .flatMap(zoo())
//                                .doOnNext(beep())
                                .map(response -> {
                                    System.out.println("поток a " + Thread.currentThread().getName() + " " + response);
                                    return fluxRoutedEnvelope.toBuilder()
                                            .key(fluxRoutedEnvelope.getKey())
                                            .value(response)
                                            .routeToAdapter("test-responses")
                                            .build();
                                })
                                .onErrorResume(throwable ->  Mono.just(fluxRoutedEnvelope.toBuilder()
                                        .key(fluxRoutedEnvelope.getKey())
                                        .value(throwable)
                                        .routeToAdapter("test-responses")
                                        .build()))
                )
//                .cast(OutgoingResponseEnvelope.class)
                .doOnNext(requestResponseAdapter.routeEnvelopeToFlux())
                .subscribe();

        // serves all requests to database
        fluxManagerRoutedEnvelope.getStream("test-responses")
                .log("333333")
                .flatMap(requestResponseAdapter.buildResponses())
                .doOnNext(requestResponseAdapter.publishResponseToMono())
                .subscribe();


    }

    @SneakyThrows
    private TestDto foo(TestDto testDto) {

        Random random = new Random();
        if (random.nextBoolean()){
            System.out.println("поток a " + Thread.currentThread().getName() + " " + testDto);
            return testDto;
        } else {
            System.out.println("----error " + testDto);
            throw new Exception("suka");
        }

    }

    private Consumer<TestDto> beep() {

        return testDto -> Mono.just(testDto)
                .doOnNext(t -> {
                    for (int i = 0; i < 100000; i++){
                        if (i==1000) System.out.println("поток b " + Thread.currentThread().getName() + " " + t);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Function<TestDto, Mono<TestDto>> zoo() {

        return testDto -> Mono.just(testDto)
                .doOnNext(t -> {
                    for (int i = 0; i < 100000; i++){
                        if (i==1000) System.out.println("поток b " + Thread.currentThread().getName() + " " + t);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());

    }

}

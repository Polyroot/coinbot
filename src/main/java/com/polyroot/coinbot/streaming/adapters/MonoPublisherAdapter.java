package com.polyroot.coinbot.streaming.adapters;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.EnumMap;
import java.util.function.Consumer;

import static reactor.core.publisher.Sinks.EmitResult.*;


public class MonoPublisherAdapter {

    @Getter
    private String adapterName;

    private final Logger logger = LoggerFactory.getLogger("FLUX ADAPTER");

    private Sinks.One loopbackSink;
    @Getter
    private Mono monoToSubscribe;

    public MonoPublisherAdapter(String adapterName){
        this.loopbackSink = Sinks.one();
        this.monoToSubscribe = this.loopbackSink.asMono()
                .publishOn(Schedulers.boundedElastic()); // let it be a cold source, or use .share(); // if we want a hot source
        this.adapterName = adapterName;
    }

    // firstly it's only a monitoring, later there could be some handlers implementing additional logic
    private EnumMap<Sinks.EmitResult, Consumer> emitResultHandlers = new EnumMap<>(Sinks.EmitResult.class){
        {
            put(FAIL_TERMINATED,logErrorStatus("FAIL_TERMINATED"));
            put(FAIL_OVERFLOW, logErrorStatus("FAIL_OVERFLOW"));
            put(FAIL_CANCELLED, logErrorStatus("FAIL_OVERFLOW"));
            put(FAIL_NON_SERIALIZED, msg -> {
                // when multiple threads try to write simultaneously, fail fast and retry
                // which is an optimistic scenarion in comparison with using a synchronized lock
                loopbackSink.emitValue(msg, (type, result) -> {
                    logErrorStatus("AGAIN FAIL_NON_SERIALIZED");
                    return true;
                });
                logErrorStatus("RETRY FAIL_NON_SERIALIZED");
            });
            put(FAIL_ZERO_SUBSCRIBER,logErrorStatus("FAIL_ZERO_SUBSCRIBER"));
        }
    };

    @Getter
    private Consumer monoInput = item -> {
        var emitResult = loopbackSink.tryEmitValue(item);
    };

    private String adapterStatusMsgTemplate = "[%s ADAPTER]: %s , %s";

    private Consumer logErrorStatus(String status){
        return
                input -> logger.info(
                        String.format(
                                adapterStatusMsgTemplate,
                                adapterName.toUpperCase(),
                                "ERROR",
                                status)
                );
    }
}

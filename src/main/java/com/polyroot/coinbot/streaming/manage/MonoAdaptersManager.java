package com.polyroot.coinbot.streaming.manage;

import com.polyroot.coinbot.streaming.adapters.MonoPublisherAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// some kind of a dynamic storage for publisher adapters
// which could be used for connecting the reactive requests with responses
public class MonoAdaptersManager {
    private Map<String, MonoPublisherAdapter> requestStreams = new HashMap<>();

    private final String unitName = "mono wiring manager";
    private final Logger logger = LoggerFactory.getLogger(unitName);

    public MonoPublisherAdapter newWire(String wireId){
        MonoPublisherAdapter stream = null;
        if (!requestStreams.containsKey(wireId)){
            stream = new MonoPublisherAdapter(wireId);
            requestStreams.put(wireId, stream);
        }
        return stream;
    }

    private MonoPublisherAdapter wireOnDemand(String wireId){
        MonoPublisherAdapter result = null;
        if (!requestStreams.containsKey(wireId)){
            result = newWire(wireId);
            logger.info(String.format("New request wiring: %s", wireId));
        } else {
            result = requestStreams.get(wireId);
        }
        return result;
    }

    // get the object sink for publishing the items
    public Consumer getInput(String wireId){
        return wireOnDemand(wireId).getMonoInput();
    }

    // get the object flux for sending it to the clients
    public Mono getOutput(String wireId){
        return wireOnDemand(wireId).getMonoToSubscribe()
                .doOnSuccess(removeWire(wireId));
    }

    private Consumer removeWire(String wireId) {
        return input -> requestStreams.remove(wireId);
    }
}

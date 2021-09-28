package com.polyroot.coinbot.streaming.manage;

import com.polyroot.coinbot.streaming.adapters.FluxPublisherAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// some kind of a dynamic storage for publisher adapters
// which could be used for connecting the reactive streams
// with a number of clients
public class FluxAdaptersManager<T> {
    private final Map<String, FluxPublisherAdapter<T>> requestStreams = new HashMap<>();

    private final String unitName = "flux manager";
    private final Logger logger = LoggerFactory.getLogger(unitName);

    public FluxPublisherAdapter<T> newStream(String streamName){
        FluxPublisherAdapter<T> stream = null;
        if (!requestStreams.containsKey(streamName)){
            stream = new FluxPublisherAdapter<T>(streamName);
            requestStreams.put(streamName, stream);
        }
        return stream;
    }

    private FluxPublisherAdapter<T> streamOnDemand(String streamName){
        FluxPublisherAdapter<T> result = null;
        if (!requestStreams.containsKey(streamName)){
            result = newStream(streamName);
            logger.info(String.format("New stream: %s", streamName));
        } else {
            result = requestStreams.get(streamName);
        }
        return result;
    }

    // get the object sink for publishing the items
    public Consumer<T> getSink(String streamName){
        return streamOnDemand(streamName).getStreamInput();
    }

    // get the object flux for sending it to the clients
    public Flux<T> getStream(String streamName){
        return streamOnDemand(streamName).getStreamToSubscribe();
    }

}

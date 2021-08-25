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
public class FluxAdaptersManager {
    private Map<String, FluxPublisherAdapter> requestStreams = new HashMap<>();

    private final String unitName = "flux manager";
    private final Logger logger = LoggerFactory.getLogger(unitName);

    public FluxPublisherAdapter newStream(String streamName){
        FluxPublisherAdapter stream = null;
        if (!requestStreams.containsKey(streamName)){
            stream = new FluxPublisherAdapter(streamName);
            requestStreams.put(streamName, stream);
        }
        return stream;
    }

    private FluxPublisherAdapter streamOnDemand(String streamName){
        FluxPublisherAdapter result = null;
        if (!requestStreams.containsKey(streamName)){
            result = newStream(streamName);
            logger.info(String.format("New stream: %s", streamName));
        } else {
            result = requestStreams.get(streamName);
        }
        return result;
    }

    // get the object sink for publishing the items
    public Consumer getSink(String streamName){
        return streamOnDemand(streamName).getStreamInput();
    }

    // get the object flux for sending it to the clients
    public Flux getStream(String streamName){
        return streamOnDemand(streamName).getStreamToSubscribe();
    }

}

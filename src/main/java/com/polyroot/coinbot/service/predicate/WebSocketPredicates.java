package com.polyroot.coinbot.service.predicate;

import com.fasterxml.jackson.databind.JsonNode;
import com.polyroot.coinbot.model.EventType;

import java.util.Objects;
import java.util.function.Predicate;

public class WebSocketPredicates {

    public final static Predicate<JsonNode> isEvent = payloadAsJsonNode
            -> payloadAsJsonNode.has("e");

    public final static Predicate<JsonNode> isResult = payloadAsJsonNode
            -> payloadAsJsonNode.has("result");

    public static Predicate<EventType> eventTypePredicate(JsonNode payloadAsJsonNode) {
        return eventType -> Objects.equals(payloadAsJsonNode.get("e").asText(), eventType.getEvent());
    }

}

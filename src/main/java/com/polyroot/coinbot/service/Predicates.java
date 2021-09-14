package com.polyroot.coinbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.polyroot.coinbot.model.EventType;
import com.polyroot.coinbot.model.dto.Method;

import java.util.Objects;
import java.util.function.Predicate;

import static com.polyroot.coinbot.model.EventType.*;

public class Predicates {

    public final static Predicate<JsonNode> isEvent = payloadAsJsonNode
            -> payloadAsJsonNode.has("e");

    public final static Predicate<JsonNode> isResult = payloadAsJsonNode
            -> payloadAsJsonNode.has("result");

    public static Predicate<EventType> eventTypePredicate(JsonNode payloadAsJsonNode) {
        return eventType -> Objects.equals(payloadAsJsonNode.get("e").asText(), eventType.getEvent());
    }

}

package com.polyroot.coinbot.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Predicate;

public class Predicates {

    public final static Predicate<JsonNode> isEvent = payloadAsJsonNode -> payloadAsJsonNode.has("e");
    public final static Predicate<JsonNode> isResult = payloadAsJsonNode -> payloadAsJsonNode.has("result");

}

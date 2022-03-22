package com.polyroot.coinbot.model.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder(toBuilder = true)
public class FluxRoutedEnvelope {
    private String key;
    private Object value;
    private String routeToAdapter;
}

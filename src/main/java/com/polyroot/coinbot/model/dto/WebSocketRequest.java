package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Slf4j
public class WebSocketRequest {

    private Method method;
    private Set<String> params;
    private Integer id;

    public WebSocketRequest(Method method, Set<String> params) {
        this.method = method;
        this.params = params;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.debug(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static WebSocketRequest mappingWebSocketRequest(Method method, String pair, String... dataStream) {
        Set<String> params = mappingParams(pair, dataStream);
        return new WebSocketRequest(method, params);
    }

    private static Set<String> mappingParams(String pair, String... dataStream) {
        return Arrays.stream(dataStream)
                .map(ds -> MessageFormat.format("{0}@{1}", pair, ds))
                .collect(Collectors.toSet());
    }

}

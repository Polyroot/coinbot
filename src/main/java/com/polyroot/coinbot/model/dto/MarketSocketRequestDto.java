package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Slf4j
@ToString
public class MarketSocketRequestDto {

    private Method method;
    @JsonInclude(Include.NON_NULL)
    private Set<String> params;
    private Integer id;

    public String toString(ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.debug(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

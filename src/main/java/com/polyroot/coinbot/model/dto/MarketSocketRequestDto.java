package com.polyroot.coinbot.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Slf4j
public class MarketSocketRequestDto {

    private Method method;
    private Set<String> params;
    private Integer id;

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
}

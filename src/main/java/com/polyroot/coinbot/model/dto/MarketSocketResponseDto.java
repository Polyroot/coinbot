package com.polyroot.coinbot.model.dto;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Slf4j
@ToString
public class MarketSocketResponseDto {

    private Object result;
    private Integer id;

}

package com.polyroot.coinbot.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Slf4j
public class WebSocketResponse {

    private String result;
    private Integer id;

}

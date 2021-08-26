package com.polyroot.coinbot.model.document;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
public class MarketSocketResponse {

    private Object result;
    private Integer requestId;
    @Id
    private String id;

}

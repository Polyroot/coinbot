package com.polyroot.coinbot.model.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.polyroot.coinbot.model.dto.Method;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
public class MarketSocketRequest {

    private Method method;
    @JsonInclude(Include.NON_NULL)
    private Set<String> params;
    private Integer requestId;
    @Id
    private String id;

}

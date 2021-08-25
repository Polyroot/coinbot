package com.polyroot.coinbot.model.document;

import com.polyroot.coinbot.model.dto.Method;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
public class MarketSocketRequest {

    private Method method;
    private Set<String> params;
    private Integer requestId;
    @Id
    private String id;

}

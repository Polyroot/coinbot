package com.polyroot.coinbot.route;

import com.polyroot.coinbot.handler.StreamHandlerIml;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class StreamRouter {

    @Bean
    public RouterFunction<ServerResponse> route(StreamHandlerIml streamHandlerIml) {

        RequestPredicate routeMessage = RequestPredicates.POST("/start")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON));

        return RouterFunctions
                .route(routeMessage, streamHandlerIml::start);
    }

}

package com.polyroot.coinbot.route;

import com.polyroot.coinbot.handler.StreamHandlerIml;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StreamRouter {

    @Bean
    public RouterFunction<ServerResponse> composedRoutes(StreamHandlerIml streamHandlerIml) {
        return nest(path("/pegas"),
                nest(path("/cjs"),
                        route()
                                .POST("/", accept(MediaType.APPLICATION_JSON), streamHandlerIml::test)
                                .build()
                )
                .andNest(path("/exp"),
                        route()
                                .POST("/", accept(MediaType.APPLICATION_JSON), streamHandlerIml::test)
                                .build()
                )
        );
    }

}

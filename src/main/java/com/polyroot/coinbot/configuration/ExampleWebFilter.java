package com.polyroot.coinbot.configuration;

import org.springframework.cloud.sleuth.Span;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class ExampleWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {


            Span span = serverWebExchange.getAttribute(Span.class.getName());

        return webFilterChain
                .filter(serverWebExchange)
                .contextWrite(ctx -> ctx.put("requestId", span.context().traceId()));

    }
}

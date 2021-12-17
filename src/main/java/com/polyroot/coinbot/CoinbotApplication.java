package com.polyroot.coinbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication
//@EnableWebFlux
@EnableReactiveMongoRepositories
public class CoinbotApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(CoinbotApplication.class, args);
    }

}

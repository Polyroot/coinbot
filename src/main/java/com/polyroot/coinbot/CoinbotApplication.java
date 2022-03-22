package com.polyroot.coinbot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polyroot.coinbot.model.dto.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
//@EnableWebFlux
@EnableReactiveMongoRepositories
public class CoinbotApplication {

    public static void main(String[] args) {

        Flux.range(1, 5)
                        .doOnNext(System.out::println)
                                .doOnComplete(() -> System.out.println("hello "))
                                        .subscribe();

        Hooks.onOperatorDebug();
        SpringApplication.run(CoinbotApplication.class, args);
    }

}

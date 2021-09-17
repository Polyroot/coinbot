package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.MiniTicker;
import com.polyroot.coinbot.model.document.Ticker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TickerRepository extends ReactiveMongoRepository<Ticker, String> {
}

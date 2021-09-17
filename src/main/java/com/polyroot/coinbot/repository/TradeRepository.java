package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.Trade;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TradeRepository extends ReactiveMongoRepository<Trade, String> {
}

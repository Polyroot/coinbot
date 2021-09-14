package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.AggTrade;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AggTradeRepository extends ReactiveMongoRepository<AggTrade, String> {
}

package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.MiniTicker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MiniTickerRepository extends ReactiveMongoRepository<MiniTicker, String> {
}

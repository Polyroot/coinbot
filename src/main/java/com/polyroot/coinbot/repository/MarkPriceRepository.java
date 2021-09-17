package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.MarkPrice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MarkPriceRepository extends ReactiveMongoRepository<MarkPrice, String> {
}

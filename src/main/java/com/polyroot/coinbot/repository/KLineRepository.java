package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.KLine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface KLineRepository extends ReactiveMongoRepository<KLine, String> {
}

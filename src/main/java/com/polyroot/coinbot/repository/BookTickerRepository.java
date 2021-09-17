package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.BookTicker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookTickerRepository extends ReactiveMongoRepository<BookTicker, String> {
}

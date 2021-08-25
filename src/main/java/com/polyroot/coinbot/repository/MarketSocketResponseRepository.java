package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.MarketSocketResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MarketSocketResponseRepository extends ReactiveMongoRepository<MarketSocketResponse, String> {


}

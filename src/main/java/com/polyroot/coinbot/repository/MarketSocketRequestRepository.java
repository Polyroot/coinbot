package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.MarketSocketRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MarketSocketRequestRepository extends ReactiveMongoRepository<MarketSocketRequest, String> {


}

package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.ForceOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ForceOrderRepository extends ReactiveMongoRepository<ForceOrder, String> {
}

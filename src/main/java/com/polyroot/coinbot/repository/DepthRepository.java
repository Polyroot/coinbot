package com.polyroot.coinbot.repository;

import com.polyroot.coinbot.model.document.Depth;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DepthRepository extends ReactiveMongoRepository<Depth, String> {


}

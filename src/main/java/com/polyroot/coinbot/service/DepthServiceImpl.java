package com.polyroot.coinbot.service;

import com.polyroot.coinbot.model.dto.DepthResponse;
import com.polyroot.coinbot.service.predicate.DepthPredicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepthServiceImpl {

    public void orderBookCorrectlyRule8(DepthResponse depthResponse) {
        depthResponse.getAsks().removeIf(DepthPredicates.isQuantity0);
        depthResponse.getBids().removeIf(DepthPredicates.isQuantity0);
    };

}

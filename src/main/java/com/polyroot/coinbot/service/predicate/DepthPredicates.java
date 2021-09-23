package com.polyroot.coinbot.service.predicate;


import java.util.List;
import java.util.function.Predicate;

public class DepthPredicates {

    public static Predicate<List<Float>> isQuantity0 = order -> order.contains(0.00f);

}

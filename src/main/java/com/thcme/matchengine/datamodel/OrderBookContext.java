package com.thcme.matchengine.datamodel;

import lombok.Data;

import java.util.Map;
import java.util.HashMap;

@Data
public class OrderBookContext {
    private OrderKey orderKey;
    private Map<String, Order> buyMapOfUserIdsToAggregatedOrders  = new HashMap<>();
    private Map<String, Order> sellMapOfUserIdsToAggregatedOrders = new HashMap<>();

    public OrderBookContext(OrderKey orderKey) {
        this.orderKey = orderKey;
    }
    
    
}

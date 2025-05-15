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
    
    public boolean isUserPresent(String userId) {
        return buyMapOfUserIdsToAggregatedOrders.containsKey(userId) || sellMapOfUserIdsToAggregatedOrders.containsKey(userId);
    }
    public boolean isUserPresentInBuyMap(String userId) {
        return buyMapOfUserIdsToAggregatedOrders.containsKey(userId);
    }
    public boolean isUserPresentInSellMap(String userId) {
        return sellMapOfUserIdsToAggregatedOrders.containsKey(userId);
    }
    
    
    
    
}

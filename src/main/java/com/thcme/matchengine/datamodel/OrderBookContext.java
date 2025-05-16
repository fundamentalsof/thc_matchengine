package com.thcme.matchengine.datamodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Data
public class OrderBookContext {
    private OrderKey orderKey;
    private Map<String, Order> buyMapOfUserIdsToAggregatedOrders  = new HashMap<>();
    private Map<String, Order> sellMapOfUserIdsToAggregatedOrders = new HashMap<>();

    private List<Order> sellListAcrossAllParticipants = new ArrayList();
    private List<Order> buyListAcrossAllParticipants = new ArrayList();
    

    public OrderBookContext(OrderKey orderKey) {
        this.orderKey = orderKey;
    }
    
    public void pushBuyOrder(Order order) {
        List<Order> orderInList = buyListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            buyListAcrossAllParticipants.remove(orderInList.get(0));
            buyListAcrossAllParticipants.add(order);
        }
        else{
            buyListAcrossAllParticipants.add(order);
        }
    }
    public void pushSellOrder(Order order) {
        List<Order> orderInList = sellListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            sellListAcrossAllParticipants.remove(orderInList.get(0));
            sellListAcrossAllParticipants.add(order);
        }
        else{
            sellListAcrossAllParticipants.add(order);
        }
    }

    public void removeSellOrder(Order order) {
        List<Order> orderInList = sellListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            sellListAcrossAllParticipants.remove(orderInList.get(0));
            
        }
    }

    public void removeBuyOrder(Order order) {
        List<Order> orderInList = buyListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            buyListAcrossAllParticipants.remove(orderInList.get(0));
        }
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

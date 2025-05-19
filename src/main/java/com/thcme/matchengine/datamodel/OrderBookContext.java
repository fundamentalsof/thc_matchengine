package com.thcme.matchengine.datamodel;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Data
@Getter
public class OrderBookContext {
    private OrderKey orderKey;
    private Map<String, Order> buyMapOfUserIdsToAggregatedOrders  = new HashMap<>();
    private Map<String, Order> sellMapOfUserIdsToAggregatedOrders = new HashMap<>();

    private List<Order> sellListAcrossAllParticipants = new ArrayList();
    private List<Order> buyListAcrossAllParticipants = new ArrayList();
    
    private double cumulativeBuyAmount = 0;
    private double cumulativeSellAmount = 0;

    public OrderBookContext(OrderKey orderKey) {
        this.orderKey = orderKey;
    }
    
    public void pushOrder(Order order) {
        if (order.getDirection() == Order.Direction.BUY) {
            pushBuyOrder(order);
        } else {
            pushSellOrder(order);
        }
    }
    
    private void pushSellOrder(Order order) {
        sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), order);
        List<Order> orderInList = sellListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            Order existing =  orderInList.get(0);
            cumulativeSellAmount -= existing.getAmount();
            
            sellListAcrossAllParticipants.remove(existing);
            sellListAcrossAllParticipants.add(order);
        }
        else{
            sellListAcrossAllParticipants.add(order);
        }
        cumulativeSellAmount += order.getAmount();
    }
    private void pushBuyOrder(Order order) {
        buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), order);
        List<Order> orderInList = buyListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            Order existing =  orderInList.get(0);
            cumulativeBuyAmount -= existing.getAmount();
            buyListAcrossAllParticipants.remove(existing);
            buyListAcrossAllParticipants.add(order);
        }
        else{
            buyListAcrossAllParticipants.add(order);
        }
        cumulativeBuyAmount += order.getAmount();
    }

    private  void removeSellOrder(Order order) {
        sellMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
        
        List<Order> orderInList = sellListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            if (sellListAcrossAllParticipants.remove(orderInList.get(0))) {
                cumulativeSellAmount -= order.getAmount();        
            }
            
        }
        
    }
    private  void removeBuyOrder(Order order) {
        buyMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
        List<Order> orderInList = buyListAcrossAllParticipants.stream().
                filter(o -> o.getUserId().equals(order.getUserId())).collect(Collectors.toList());
        if (orderInList.size() > 0) {
            if (buyListAcrossAllParticipants.remove(orderInList.get(0))) {
                cumulativeBuyAmount -= order.getAmount();        
            }
        }
        
    }
    

    public void removeOrder(Order order) {
        if (order.getDirection() == Order.Direction.BUY) {
            removeBuyOrder(order);
        } else {
            removeSellOrder(order);
        }
    }

    public void removeOrderFromOtherSide(Order order) {
        if (order.getDirection() == Order.Direction.BUY) {
            removeSellOrder(order);
        } else {
            removeBuyOrder(order);
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
    public Map<String, Order> getBuyMapOfUserIdsToAggregatedOrders() {
        return Collections.unmodifiableMap(buyMapOfUserIdsToAggregatedOrders);
    }
    public Map<String, Order> getSellMapOfUserIdsToAggregatedOrders() {
        return Collections.unmodifiableMap(sellMapOfUserIdsToAggregatedOrders);
    }
    public List<Order> getSellListAcrossAllParticipants() {
        return Collections.unmodifiableList(sellListAcrossAllParticipants);
    }
    public List<Order> getBuyListAcrossAllParticipants() {
        return Collections.unmodifiableList(buyListAcrossAllParticipants);
    }
}

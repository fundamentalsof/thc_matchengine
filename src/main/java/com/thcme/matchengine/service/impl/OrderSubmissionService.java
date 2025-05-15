package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderSubmissionService implements IOrderSubmissionService {

    Map<OrderKey, OrderBookContext> orderContext = new HashMap<>();
    @Override public Order addOrder(final Order order) {
        OrderKey orderKey = new OrderKey(order.getCurrencyPair(), order.getDealtCurrency(), order.getValueDate());
        OrderBookContext orderBookContext = orderContext.get(orderKey);
        if (orderBookContext == null) {
            orderBookContext = new OrderBookContext(orderKey);
            orderContext.put(orderKey, orderBookContext);
        } 
        return orderBookContext.addOrder(order);
    }
    
    public void reset(){
        orderContext.clear();
    } 
    
}

package com.thcme.matchengine.service.interfaces;

import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;

import java.util.List;

public interface IOrderBookContextService {
    OrderBookContext getOrderBookContext(OrderKey orderKey);

    List<OrderBookContext> getOrderBookContextsForUserId(String userId);
    
    void reset();
}

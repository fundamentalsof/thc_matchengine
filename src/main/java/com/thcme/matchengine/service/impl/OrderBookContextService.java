package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderBookContextService implements IOrderBookContextService {

    Map<OrderKey, OrderBookContext> mapOfOrderKeysToOrderContexts = new HashMap<>();

    
    @Override public OrderBookContext getOrderBookContext(final OrderKey orderKey) {
        OrderBookContext orderBookContext = mapOfOrderKeysToOrderContexts.get(orderKey);
        if (orderBookContext == null) {
            orderBookContext = new OrderBookContext(orderKey);
            mapOfOrderKeysToOrderContexts.put(orderKey, orderBookContext);
        }
        return orderBookContext;
    }

    @Override public List<OrderBookContext> getOrderBookContextsForUserId(final String userId) {
        return mapOfOrderKeysToOrderContexts.values().stream()
                .filter(orderBookContext -> orderBookContext.isUserPresent(userId))
                .toList();
    }

    @Override public void reset() {
        mapOfOrderKeysToOrderContexts.clear();
    }
}

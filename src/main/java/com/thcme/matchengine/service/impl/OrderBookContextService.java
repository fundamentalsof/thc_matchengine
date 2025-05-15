package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderBookContextService implements IOrderBookContextService {

    Map<OrderKey, OrderBookContext> maoOfOrderKeysToOrderContexts = new HashMap<>();

    
    @Override public OrderBookContext getOrderBookContext(final OrderKey orderKey) {
        OrderBookContext orderBookContext = maoOfOrderKeysToOrderContexts.get(orderKey);
        if (orderBookContext == null) {
            orderBookContext = new OrderBookContext(orderKey);
            maoOfOrderKeysToOrderContexts.put(orderKey, orderBookContext);
        }
        return orderBookContext;
    }

    @Override public void reset() {
        maoOfOrderKeysToOrderContexts.clear();
    }
}

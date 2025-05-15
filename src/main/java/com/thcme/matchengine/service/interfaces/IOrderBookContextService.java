package com.thcme.matchengine.service.interfaces;

import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;

public interface IOrderBookContextService {
    OrderBookContext getOrderBookContext(OrderKey orderKey);
    void reset();
}

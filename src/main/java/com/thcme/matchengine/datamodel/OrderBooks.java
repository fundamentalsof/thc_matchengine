package com.thcme.matchengine.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderBooks {
    // This class represents the order books for different currency pairs.
    // It contains methods to add orders to the order book and retrieve the order book for a specific currency pair.
    
    private final Map<OrderKey, List<Order>> orderBooks = new HashMap<>();

    
}

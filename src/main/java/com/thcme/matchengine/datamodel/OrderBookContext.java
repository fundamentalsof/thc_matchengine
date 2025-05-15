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
    
    public Order addOrder(Order order) {
        if (order.getDirection() == Order.Direction.BUY) {
            return doBuySideLogic(order);
        } else { //sell
            return doSellSideLogic(order);
        }
    }

    private Order doSellSideLogic(final Order order) {
        Order aggregatedOrder;
        Order existingOrder = sellMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        Order existingOppositeOrder = buyMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        if(existingOrder == null && existingOppositeOrder == null) {
            // no existing order
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.SELL,
                    order.getAmount(), order.getValueDate(),
                    order.getUserId());
            sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
        }
        else if (existingOrder != null) {
            // existing order, Aggregate SELLS
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.SELL,
                    existingOrder.getAmount() + order.getAmount(), order.getValueDate(),
                    order.getUserId());
            sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
        } 
        else { //existingOppositeOrder != null, Aggregate SELL and BUY, see the result and 
            // shift sides if needed
                double amount =  existingOppositeOrder.getAmount() - order.getAmount();
                if (amount < 0) { //Shift sides
                    amount = -amount;
                    buyMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
                    aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                            Order.Direction.SELL,
                            -amount, order.getValueDate(),
                            order.getUserId());
                    sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                }
                else { // amount >= 0, We stick to buy side as it had a greater amount
                    aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                            Order.Direction.BUY,
                            amount, order.getValueDate(),
                            order.getUserId());
                    buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                }
        }
       return aggregatedOrder;
       
    }

    private Order doBuySideLogic(final Order order) {
        Order aggregatedOrder;
        Order existingOrder = buyMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        Order existingOppositeOrder = sellMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        if (existingOrder == null && existingOppositeOrder == null) {
            // no existing order
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.BUY,
                    order.getAmount(), order.getValueDate(),
                    order.getUserId());
            buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
            return aggregatedOrder;
        }
        else if (existingOrder != null) {
            // existing order, Aggregate BUYs
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.BUY,
                    existingOrder.getAmount() + order.getAmount(), order.getValueDate(),
                    order.getUserId());
            buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
        } else { //existingOppositeOrder != null, Aggregate BUY and SELL, see the result and 
            // shift sides if needed
                double amount =   existingOppositeOrder.getAmount() - order.getAmount();
                if (amount < 0) { //Shift sides to SELL
                    amount = -amount;
                    sellMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
                    aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                            Order.Direction.BUY,
                            -amount, order.getValueDate(),
                            order.getUserId());
                    buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                }
                else { // amount >= 0, We stick to Sell  side as it had a greater amount
                    aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                            Order.Direction.SELL,
                            amount, order.getValueDate(),
                            order.getUserId());
                    sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                }
            }
            return aggregatedOrder;
        }
}

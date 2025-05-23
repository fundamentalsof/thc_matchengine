package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderSubmissionService implements IOrderSubmissionService {

    @Autowired
    private OrderBookContextService orderBookContextService;
    
    @Override public Order addOrder(final Order order) {
        OrderKey orderKey = new OrderKey(order.getCurrencyPair(), order.getDealtCurrency(),
                 order.getValueDate());
        return addOrderInternal(order, orderBookContextService.getOrderBookContext(orderKey));
    }

    private Order addOrderInternal(final Order order, OrderBookContext orderBookContext) {
        synchronized (orderBookContext) {
            if (order.getDirection() == Order.Direction.BUY) {
                return doBuySideLogic(order, orderBookContext);
            } else { //sell
                return doSellSideLogic(order, orderBookContext);
            }
        }
    }

    public void reset(){
        orderBookContextService.reset();
    }


    private Order doSellSideLogic(final Order order, OrderBookContext orderBookContext) {
        Order aggregatedOrder;
        final Map<String, Order> sellMapOfUserIdsToAggregatedOrders =
                    orderBookContext.getSellMapOfUserIdsToAggregatedOrders();
        final Map<String, Order> buyMapOfUserIdsToAggregatedOrders =
                orderBookContext.getBuyMapOfUserIdsToAggregatedOrders();
        Order existingOrder = sellMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        Order existingOppositeOrder = buyMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        if(existingOrder == null && existingOppositeOrder == null) {
            // no existing order
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.SELL,
                    order.getAmount(), order.getValueDate(),
                    order.getUserId());
            sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
            orderBookContext.pushSellOrder(aggregatedOrder);
        }
        else if (existingOrder != null) {
            // existing order, Aggregate SELLS
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.SELL,
                    existingOrder.getAmount() + order.getAmount(), order.getValueDate(),
                    order.getUserId());
            sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
            orderBookContext.pushSellOrder(aggregatedOrder);
        }
        else { //existingOppositeOrder != null, Aggregate SELL and BUY, see the result and 
            // shift sides if needed
            double amount =  existingOppositeOrder.getAmount() - order.getAmount();
            if (amount < 0) { //Shift sides
                amount = -amount;
                buyMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
                orderBookContext.removeBuyOrder(existingOppositeOrder);
                aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                        Order.Direction.SELL,
                        amount, order.getValueDate(),
                        order.getUserId());
                sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                orderBookContext.pushSellOrder(aggregatedOrder);
            }
            else { // amount >= 0, We stick to buy side as it had a greater amount
                aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                        Order.Direction.BUY,
                        amount, order.getValueDate(),
                        order.getUserId());
                if (amount == 0) {
                    buyMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
                    orderBookContext.removeBuyOrder(existingOppositeOrder);
                } else {
                    buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                    orderBookContext.pushBuyOrder(aggregatedOrder);
                }
            }
        }
        return aggregatedOrder;
    }

    private Order doBuySideLogic(final Order order, OrderBookContext orderBookContext) {
        Order aggregatedOrder;
        final Map<String, Order> sellMapOfUserIdsToAggregatedOrders =
                orderBookContext.getSellMapOfUserIdsToAggregatedOrders();
        final Map<String, Order> buyMapOfUserIdsToAggregatedOrders =
                orderBookContext.getBuyMapOfUserIdsToAggregatedOrders();

        Order existingOrder = buyMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        Order existingOppositeOrder = sellMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        if (existingOrder == null && existingOppositeOrder == null) {
            // no existing order
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.BUY,
                    order.getAmount(), order.getValueDate(),
                    order.getUserId());
            buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
            orderBookContext.pushBuyOrder(aggregatedOrder);
            
            return aggregatedOrder;
        }
        else if (existingOrder != null) {
            // existing order, Aggregate BUYs
            aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                    Order.Direction.BUY,
                    existingOrder.getAmount() + order.getAmount(), order.getValueDate(),
                    order.getUserId());
            buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
            orderBookContext.pushBuyOrder(aggregatedOrder);
        } else { //existingOppositeOrder != null, Aggregate BUY and SELL, see the result and 
            // shift sides if needed
            double amount =   existingOppositeOrder.getAmount() - order.getAmount();
            if (amount < 0) { //Shift sides to SELL
                amount = -amount;
                sellMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
                orderBookContext.removeSellOrder(existingOppositeOrder);
                aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                        Order.Direction.BUY,
                        amount, order.getValueDate(),
                        order.getUserId());
                buyMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                orderBookContext.pushBuyOrder(aggregatedOrder);
            }
            else { // amount >= 0, We stick to Sell  side as it had a greater amount
                aggregatedOrder = new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                        Order.Direction.SELL,
                        amount, order.getValueDate(),
                        order.getUserId());
                if (amount == 0) {
                    sellMapOfUserIdsToAggregatedOrders.remove(order.getUserId());
                    orderBookContext.removeSellOrder(existingOppositeOrder);
                } else {
                    sellMapOfUserIdsToAggregatedOrders.put(order.getUserId(), aggregatedOrder);
                    orderBookContext.pushSellOrder(aggregatedOrder);
                }
            }
        }
        return aggregatedOrder;
    }
    
}

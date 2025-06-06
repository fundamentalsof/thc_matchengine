package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return doAddOrderLogic(order, orderBookContext);
        }
    }

    public void reset(){
        orderBookContextService.reset();
    }
    
    private Order doAddOrderLogic(final Order order, OrderBookContext orderBookContext) {
        Order aggregatedOrder;
        Order.Direction orderDirection = order.getDirection();
        Order.Direction otherDirection = orderDirection == Order.Direction.BUY ?
                Order.Direction.SELL : Order.Direction.BUY;
        
        final Map<String, Order> mySideMapOfUserIdsToAggregatedOrders =
                order.getDirection() == Order.Direction.SELL ?
                orderBookContext.getSellMapOfUserIdsToAggregatedOrders():
                orderBookContext.getBuyMapOfUserIdsToAggregatedOrders();
        final Map<String, Order> otherSideMapOfUserIdsToAggregatedOrders =
                order.getDirection() == Order.Direction.SELL ?
                orderBookContext.getBuyMapOfUserIdsToAggregatedOrders() :
                        orderBookContext.getSellMapOfUserIdsToAggregatedOrders();
        Order existingOrder = mySideMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        Order existingOppositeOrder = otherSideMapOfUserIdsToAggregatedOrders.get(order.getUserId());
        if(existingOrder == null && existingOppositeOrder == null) {
            // no existing order
            aggregatedOrder = createAggregatedOrder(order, orderDirection,
                    order.getAmount());
            pushNewOrder.call(orderDirection, aggregatedOrder, orderBookContext);
        }
        else if (existingOrder != null) {
            // existing order, Aggregate SELLS
            aggregatedOrder = createAggregatedOrder(order, orderDirection,
                    existingOrder.getAmount() + order.getAmount());
            pushAggregatedOrder.call(orderDirection, aggregatedOrder, orderBookContext);
        }
        else { //existingOppositeOrder != null, Aggregate SELL and BUY, see the result and 
            // shift sides if needed
            double amount =  existingOppositeOrder.getAmount() - order.getAmount();
            if (amount < 0) { //Shift sides from existing Opposite Side to SELL
                amount = -amount;
                aggregatedOrder = createAggregatedOrder(order, orderDirection, amount);
                pushOrderAndRemoveFromOtherSide.call(orderDirection, aggregatedOrder,
                        orderBookContext);
            }
            else { // amount >= 0, We stick to buy side as it had a greater amount
                aggregatedOrder = createAggregatedOrder(order, otherDirection, amount);
                if (amount == 0) {
                    //Special case ..Zero result
                    removeOrder(existingOppositeOrder, orderBookContext);
                } else {
                    //We stick to existing Order's side with reduced amount 
                    pushAggregatedReducedOrder.call(otherDirection, aggregatedOrder,
                            orderBookContext);
                }
            }
        }
        return aggregatedOrder;
    }

    // OrderPusher is used to push new orders to the order book context
    @FunctionalInterface
    private interface OrderPusher {
        void call(final Order.Direction direction, final Order order,
                       final OrderBookContext orderBookContext);
    }
    private final OrderPusher pushNewOrder = (direction, order, orderBookContext) -> {
        orderBookContext.pushOrder(order);
    };
    private final OrderPusher pushAggregatedOrder = pushNewOrder;
    private final OrderPusher pushAggregatedReducedOrder = pushNewOrder;


    @FunctionalInterface
    private interface OrderPusherAndRemover {
        Order call(final Order.Direction direction, final Order order,
                   final OrderBookContext orderBookContext);
    }
    private final OrderPusherAndRemover pushOrderAndRemoveFromOtherSide = (direction, order,
                                                               orderBookContext) -> {
        orderBookContext.removeOrderFromOtherSide(order);
        orderBookContext.pushOrder(order);
        return order;
    };
    
    private void removeOrder(final Order order, OrderBookContext orderBookContext) {
        orderBookContext.removeOrder(order);
    }
    
    private Order createAggregatedOrder(final Order order, Order.Direction direction,
                                        double amount) {
        return new Order(order.getCurrencyPair(), order.getDealtCurrency(),
                direction, amount, order.getValueDate(),order.getUserId());
    }
}

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
            aggregatedOrder = createAggregatedOrder(order, Order.Direction.SELL,
                    order.getAmount());
            pushNewOrder.call(Order.Direction.SELL, aggregatedOrder, orderBookContext);
        }
        else if (existingOrder != null) {
            // existing order, Aggregate SELLS
            aggregatedOrder = createAggregatedOrder(order, Order.Direction.SELL,
                    existingOrder.getAmount() + order.getAmount());
            pushAggregatedOrder.call(Order.Direction.SELL, aggregatedOrder, orderBookContext);
        }
        else { //existingOppositeOrder != null, Aggregate SELL and BUY, see the result and 
            // shift sides if needed
            double amount =  existingOppositeOrder.getAmount() - order.getAmount();
            if (amount < 0) { //Shift sides from existing Opposite Side to SELL
                amount = -amount;      
                aggregatedOrder = createAggregatedOrder(order, Order.Direction.SELL, amount);   
                pushOrderAndRemoveFromOtherSide.call(Order.Direction.SELL, aggregatedOrder,
                        orderBookContext);
            }
            else { // amount >= 0, We stick to buy side as it had a greater amount
                aggregatedOrder = createAggregatedOrder(order, Order.Direction.BUY, amount);
                if (amount == 0) {
                    //Special case ..Zero result
                    removeOrder(existingOppositeOrder, orderBookContext);
                } else {
                    //We stick to existing Order's side with reduced amount 
                    pushAggregatedReducedOrder.call(Order.Direction.BUY, aggregatedOrder,
                            orderBookContext);
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
            aggregatedOrder = createAggregatedOrder(order, Order.Direction.BUY,
                    order.getAmount());
            pushNewOrder.call(Order.Direction.BUY, aggregatedOrder, orderBookContext);
        }
        else if (existingOrder != null) {
            // existing order, Aggregate BUYs
            aggregatedOrder = createAggregatedOrder(order, Order.Direction.BUY,
                    existingOrder.getAmount() + order.getAmount());
            pushAggregatedOrder.call(Order.Direction.BUY, aggregatedOrder, orderBookContext);
        } else { //existingOppositeOrder != null, Aggregate BUY and SELL, see the result and 
            // shift sides if needed
            double amount =   existingOppositeOrder.getAmount() - order.getAmount();
            if (amount < 0) { //Shift sides to SELL
                amount = -amount;
                aggregatedOrder = createAggregatedOrder(order, Order.Direction.BUY, amount);
                pushOrderAndRemoveFromOtherSide.call(Order.Direction.BUY, aggregatedOrder,
                        orderBookContext);
            }
            else { // amount >= 0, We stick to Sell  side as it had a greater amount
                aggregatedOrder = createAggregatedOrder(order, Order.Direction.SELL, amount);
                if (amount == 0) {
                    //Special case ..Zero result
                    removeOrder(existingOppositeOrder, orderBookContext);
                } else {
                    //We have a positive amount, so we can push the order
                    pushAggregatedReducedOrder.call(Order.Direction.SELL, aggregatedOrder,
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

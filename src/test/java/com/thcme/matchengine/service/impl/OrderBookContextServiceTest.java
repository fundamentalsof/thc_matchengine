package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderBookContextServiceTest {
    @Autowired
    private OrderBookContextService orderBookContextService;
    
    //setup
    @BeforeEach
    public void setUp() {
        // This method will be called before each test case
        // You can initialize any required objects or state here
        // For example, you can create a new OrderBookContextService instance
        // or set up any necessary dependencies.
        orderBookContextService.reset();
    }
    
    @Test
    public void testOrderBookContextService() {
     
        OrderKey orderKey = new OrderKey("EURUSD", "USD",  20200101);

        OrderBookContext  context  = orderBookContextService.getOrderBookContext(orderKey);
        Assertions.assertNotNull(context );
        Assertions.assertEquals(context.getOrderKey(), orderKey);
        Assertions.assertEquals(context.getSellMapOfUserIdsToAggregatedOrders().size(), 0);
        Assertions.assertEquals(context.getBuyMapOfUserIdsToAggregatedOrders().size(), 0);
     
    }

    @Test
    public void testOrderBookContextServiceForUser() {
        OrderKey orderKey = new OrderKey("EURUSD", "USD",  20200101);

        OrderBookContext  context  = orderBookContextService.getOrderBookContext(orderKey);
        Assertions.assertNotNull(context );
        Assertions.assertEquals(context.isUserPresentInBuyMap("FOO"),false);
        Assertions.assertEquals(context.isUserPresentInSellMap("FOO"),false);
        Assertions.assertEquals(context.isUserPresent("FOO"),false);
    }


    @Test
    public void testOrderBookContextServiceForUserTrueBuy() {

        OrderKey orderKey = new OrderKey("EURUSD", "USD",  20200101);
        

        OrderBookContext  context  = orderBookContextService.getOrderBookContext(orderKey);
        context.pushOrder( 
                new Order("EURUSD", 
            "USD",
                Order.Direction.BUY, 10.0d, 20200101, "User 1"));
        Assertions.assertNotNull(context );
        
        Assertions.assertEquals(context.isUserPresentInBuyMap("User 1"),true);
        Assertions.assertEquals(context.isUserPresentInSellMap("User 1"),false);
        Assertions.assertEquals(context.isUserPresent("User 1"),true);
    }

    @Test
    public void testOrderBookContextServiceForUserTrueSell() {

        OrderKey orderKey = new OrderKey("EURUSD", "USD",  20200101);


        OrderBookContext  context  = orderBookContextService.getOrderBookContext(orderKey);
        context.pushOrder(
                new Order("EURUSD",
                        "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "User 1"));
        Assertions.assertNotNull(context );

        Assertions.assertEquals(context.isUserPresentInBuyMap("User 1"),true);
        Assertions.assertEquals(context.isUserPresentInSellMap("User 1"),false);
        Assertions.assertEquals(context.isUserPresent("User 1"),true);
    }

    @Test
    public void testOrderBookContextServiceForUserTrueBoth() {

        OrderKey orderKey = new OrderKey("EURUSD", "USD",  20200101);


        OrderBookContext  context  = orderBookContextService.getOrderBookContext(orderKey);
        context.pushOrder(
                new Order("EURUSD",
                        "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "User 1"));

        context.pushOrder(
                new Order("EURUSD",
                        "USD",
                        Order.Direction.SELL, 10.0d, 20200101, "User 1"));
        Assertions.assertNotNull(context );

        Assertions.assertEquals(context.isUserPresentInBuyMap("User 1"),true);
        Assertions.assertEquals(context.isUserPresentInSellMap("User 1"),true);
        Assertions.assertEquals(context.isUserPresent("User 1"),true);
    }

}

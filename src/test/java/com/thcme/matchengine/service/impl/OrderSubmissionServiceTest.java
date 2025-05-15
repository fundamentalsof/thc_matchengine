package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderSubmissionServiceTest {
   
    @Autowired  
    IOrderSubmissionService orderSumissionService;
    
    //setup
    @BeforeEach
    public void setup() {
        // Initialize the order submission service or any other dependencies
        orderSumissionService.reset();
        
    }
    // teardown
    
    @Test
    @DisplayName("Test First Order Submission Buy")
    public void testFirstOrderSubmissionBuy() {
        // Test the order submission functionality
        
        Order newOrder = new Order(
                "EURUSD", "USD", 
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrder);
        Assertions.assertEquals(newOrder, order);
        
    }
    @Test
    @DisplayName("Test First Order Submission Sell`")
    public void testFirstOrderSubmissionSell() {
        // Test the order submission functionality

        Order newOrder = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrder);
        Assertions.assertEquals(newOrder, order);

    }


    @Test
    @DisplayName("Test Second Order Submission - Same Side Buy")
    public void testSecondOrderSubmissionSameSideBuy() {
        // Test the order submission functionality

        Order newOrder = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrder);
        Order newerOrder = orderSumissionService.addOrder(order);
        
        Assertions.assertEquals(newerOrder.getAmount(),
                order.getAmount()+order.getAmount());

    }


    @Test
    @DisplayName("Test Second Order Submission - Same Side Sell")
    public void testSecondOrderSubmissionSameSideSell() {
        // Test the order submission functionality

        Order newOrder = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrder);
        Order newerOrder = orderSumissionService.addOrder(order);

        Assertions.assertEquals(newerOrder.getAmount(),
                order.getAmount()+order.getAmount());

    }


    @Test
    @DisplayName("Test Two diff Currency pairs -  Order Submission Sell")
    public void testTwoDiffCurrencyPairsSubmissionSell() {
        // Test the order submission functionality

        Order newOrderC1 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "USDJPY", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order2 = orderSumissionService.addOrder(newOrderC2);
        Assertions.assertEquals(newOrderC2, order2);

    }


    @Test
    @DisplayName("Test Two adds Swap Sides BUY TO SELL")
    public void testTwoAddsSwapSidesBuyToSell() {
        // Test the order submission functionality

        Order newOrderC1 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1500, 2, "user123");
        Order order2 = orderSumissionService.addOrder(newOrderC2);
        Assertions.assertEquals(order2.getAmount(), 500);
        Assertions.assertEquals(order2.getDirection(), Order.Direction.SELL);

    }


    @Test
    @DisplayName("Test Two adds Swap Sides SELL To BUY" )
    public void testTwoAddsSwapSidesSellToBuy() {
        // Test the order submission functionality

        Order newOrderC1 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1500, 2, "user123");
        Order order2 = orderSumissionService.addOrder(newOrderC2);
        Assertions.assertEquals(order2.getAmount(), 500);
        Assertions.assertEquals(order2.getDirection(), Order.Direction.BUY);

    }


}

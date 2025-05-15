package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OrderKey;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
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
    IOrderSubmissionService orderSubmissionService;
    
    @Autowired
    IOrderBookContextService orderBookContextService;
    
    //setup
    @BeforeEach
    public void setup() {
        // Initialize the order submission service or any other dependencies
        orderSubmissionService.reset();
        
    }
    // teardown
    
    @Test
    @DisplayName("Test First Order Submission Buy")
    public void testFirstOrderSubmissionBuy() {
        // Test the order submission functionality
        
        Order newOrder = new Order(
                "EURUSD", "USD", 
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSubmissionService.addOrder(newOrder);
        Assertions.assertEquals(newOrder, order);
        
    }
    @Test
    @DisplayName("Test First Order Submission Sell`")
    public void testFirstOrderSubmissionSell() {
        // Test the order submission functionality

        Order newOrder = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order = orderSubmissionService.addOrder(newOrder);
        Assertions.assertEquals(newOrder, order);

    }


    @Test
    @DisplayName("Test Second Order Submission - Same Side Buy")
    public void testSecondOrderSubmissionSameSideBuy() {
        // Test the order submission functionality

        Order newOrder = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSubmissionService.addOrder(newOrder);
        Order newerOrder = orderSubmissionService.addOrder(order);
        
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
        Order order = orderSubmissionService.addOrder(newOrder);
        Order newerOrder = orderSubmissionService.addOrder(order);

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
        Order order = orderSubmissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "USDJPY", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order2 = orderSubmissionService.addOrder(newOrderC2);
        Assertions.assertEquals(newOrderC2, order2);

    }


    @Test
    @DisplayName("Test Two adds Swap Sides BUY TO SELL")
    public void testTwoAddsSwapSidesBuyToSell() {
        // Test the order submission functionality

        Order newOrderC1 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSubmissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1500, 2, "user123");
        Order order2 = orderSubmissionService.addOrder(newOrderC2);
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
        Order order = orderSubmissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1500, 2, "user123");
        Order order2 = orderSubmissionService.addOrder(newOrderC2);
        Assertions.assertEquals(order2.getAmount(), 500);
        Assertions.assertEquals(order2.getDirection(), Order.Direction.BUY);

        OrderKey orderKey = new OrderKey("EURUSD", "USD",   2);

        OrderBookContext context = orderBookContextService.getOrderBookContext(orderKey);
        Assertions.assertEquals(context.isUserPresent("user123"), true);
        Assertions.assertEquals(context.isUserPresentInSellMap("user123"), false);
        Assertions.assertEquals(context.isUserPresentInBuyMap("user123"), true);

    }


    @Test
    @DisplayName("Test Two adds Swap Sides SELL To BUY ZERO FLAT" )
    public void testTwoAddsSwapSidesSellToBuyZEROFlat() {
        // Test the order submission functionality

        Order newOrderC1 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 1000, 2, "user123");
        Order order = orderSubmissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);

        Order newOrderC2 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 1000, 2, "user123");
        Order order2 = orderSubmissionService.addOrder(newOrderC2);
        Assertions.assertEquals(order2.getAmount(), 0);
        Assertions.assertEquals(order2.getDirection(), Order.Direction.SELL);
        
        OrderKey orderKey = new OrderKey("EURUSD", "USD",   2);
                
        OrderBookContext context = orderBookContextService.getOrderBookContext(orderKey);
        Assertions.assertEquals(context.isUserPresent("user123"), false);

    }

    @Test
    @DisplayName("Acceptance Criteria Scenario A First Order" )
    public void testAcceptanceCriteriaScenarioA() {
        // Test the order submission functionality

        Order newOrderC1 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 10000, 20250130, "User A");
        Order order = orderSubmissionService.addOrder(newOrderC1);
        Assertions.assertEquals(newOrderC1, order);
    }

    @Test
    @DisplayName("Acceptance Criteria Scenario A Second Order" )
    public void testAcceptanceCriteriaScenarioASecondOrder() {
        // Test the second order submission functionality

        Order newOrder1 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 10000, 20250130, "User A");
        Order order = orderSubmissionService.addOrder(newOrder1);

        Order newOrder2 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 5000, 20250130, "User A");
        Order order2 = orderSubmissionService.addOrder(newOrder2);
        Assertions.assertEquals(order2.getDirection(), Order.Direction.SELL);
        Assertions.assertEquals(order2.getCurrencyPair(), "EURUSD");
        Assertions.assertEquals(order2.getDealtCurrency(), "USD");
        Assertions.assertEquals(order2.getValueDate(), 20250130);
        Assertions.assertEquals(order2.getUserId(), "User A");
        Assertions.assertEquals(order2.getAmount(), 5000);
        
    }


    @Test
    @DisplayName("Acceptance Criteria Scenario A Third Order" )
    public void testAcceptanceCriteriaScenarioAThirdOrder() {
        // Test the order submission functionality


        Order newOrder1 = new Order(
                "EURUSD", "USD",
                Order.Direction.SELL, 10000, 20250130, "User A");
        Order order = orderSubmissionService.addOrder(newOrder1);

        Order newOrder2 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 5000, 20250130, "User A");
        Order order2 = orderSubmissionService.addOrder(newOrder2);

        Order newOrder3 = new Order(
                "EURUSD", "USD",
                Order.Direction.BUY, 5000, 20250130, "User B");
        Order order3 = orderSubmissionService.addOrder(newOrder3);
        Assertions.assertEquals(newOrder3, order3);
    }





}

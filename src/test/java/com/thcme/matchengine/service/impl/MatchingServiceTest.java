package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.interfaces.IMatchingService;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MatchingServiceTest {
    @Autowired IMatchingService matchingService;
    @Autowired IOrderBookContextService orderBookContextService;
    @Autowired IOrderSubmissionService orderSubmissionService;
    
    
    @BeforeEach
    public void setUp() {
        // Clear the order book before each test
        orderBookContextService.reset();
    }
    
    @Test
    public void testMatchingService() {
        OverallMatchResult matchResult = matchingService.getMatchedPositions("FOO");
        Assertions.assertNotNull(matchResult, "Match result should not be null");
        Assertions.assertEquals(0, matchResult.getMatchedPositions().size(), "Matched orders should be empty");
    }
    @Test
    public void testMatchingServiceWithSomeOrders() {
        // Add some orders to the order book
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "user123"));
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 10.0d, 20200101, "user456"));


        // Perform matching
        OverallMatchResult matchResult = matchingService.getMatchedPositions("user123");
        System.out.println("Matched orders: " + matchResult.getMatchedPositions());
        
        // Check that the matched positions are as expected
        Assertions.assertNotNull(matchResult, "Match result should not be null");
        Assertions.assertEquals(1, matchResult.getMatchedPositions().size(), "Matched orders should contain one position");
    }

    @Test
    public void testMatchingServiceWithSomeOrdersPartial() {
        // Add some orders to the order book
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "user123"));
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 7.0d, 20200101, "user456"));


        // Perform matching
        OverallMatchResult matchResult = matchingService.getMatchedPositions("user123");
        System.out.println("Matched orders: " + matchResult.getMatchedPositions());

        OverallMatchResult matchResultOther = matchingService.getMatchedPositions("user456");
        System.out.println("Matched orders: " + matchResultOther.getMatchedPositions());


        // Check that the matched positions are as expected
        Assertions.assertNotNull(matchResult, "Match result should not be null");
        Assertions.assertEquals(1, matchResult.getMatchedPositions().size(), "Matched orders should contain one position");
    }

    @Test
    public void testMatchingServiceWithSomeOrdersPartialMultipleCurrencyPairs() {
        // Add some orders to the order book
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "user123"));
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 7.0d, 20200101, "user456"));

        orderSubmissionService.addOrder(
                new Order("USDJPY", "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "user123"));
        orderSubmissionService.addOrder(
                new Order("USDJPY", "USD",
                        Order.Direction.SELL, 8.0d, 20200101, "user456"));

        orderSubmissionService.addOrder(
                new Order("AUDUSD", "USD",
                        Order.Direction.BUY, 10.0d, 20200101, "user123"));


        // Perform matching
        OverallMatchResult matchResult = matchingService.getMatchedPositions("user123");
        System.out.println("Matched orders: " + matchResult.getMatchedPositions());
        Assertions.assertEquals(3, matchResult.getMatchedPositions().size(), "Matched orders " +
                "should contain two positions");

        OverallMatchResult matchResultOther = matchingService.getMatchedPositions("user456");
        System.out.println("Matched orders: " + matchResultOther.getMatchedPositions());
        Assertions.assertEquals(2, matchResultOther.getMatchedPositions().size(), "Matched orders should contain two positions");


        // Check that the matched positions are as expected
        Assertions.assertNotNull(matchResult, "Match result should not be null");
        Assertions.assertEquals(3, matchResult.getMatchedPositions().size(), "Matched orders " +
                "should contain TWO positions");
    }

    @Test
    @DisplayName("Test matching service from Sample in Doc")
    public void testMatchingServiceFromSampleInDoc() {
        // Add some orders to the order book
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.BUY, 1000.0d, 20200101, "User A"));
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 500.0d, 20200101, "User B"));

        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 500.0d, 20200101, "User C"));

        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 500.0d, 20200101, "User D"));

        

        OverallMatchResult matchResultA = matchingService.getMatchedPositions("User A");
        System.out.println("Matched orders A : " + matchResultA.getMatchedPositions());
        Assertions.assertTrue(
        matchResultA.getMatchedPositions().get(0).getMatchedPositionAsPercentage()==100.0d);

        OverallMatchResult matchResultB = matchingService.getMatchedPositions("User B");
        System.out.println("Matched orders B : " + matchResultB.getMatchedPositions());
        Assertions.assertTrue(matchResultB.getMatchedPositions().get(0).getMatchedPositionAsPercentage()==100.0d);
        
        OverallMatchResult matchResultC = matchingService.getMatchedPositions("User C");
        System.out.println("Matched orders C : " + matchResultC.getMatchedPositions());
        Assertions.assertTrue(matchResultC.getMatchedPositions().get(0).getMatchedPositionAsPercentage()==100.0d);

        OverallMatchResult matchResultD = matchingService.getMatchedPositions("User D");
        System.out.println("Matched orders D : " + matchResultD.getMatchedPositions());
        Assertions.assertTrue(matchResultD.getMatchedPositions().get(0).getMatchedPositionAsPercentage()==0.0d);
        
    }

    @Test
    @DisplayName("Test matching service from Scenario B")
    public void testMatchingServiceFromScenarioB() {
        // Add some orders to the order book
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.SELL, 10000.0d, 20200101, "User A"));
        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.BUY, 5000.0d, 20200101, "User A"));

        orderSubmissionService.addOrder(
                new Order("EURUSD", "USD",
                        Order.Direction.BUY, 20000.0d, 20200101, "User C"));

        OverallMatchResult matchResultA = matchingService.getMatchedPositions("User A");
        System.out.println("Matched orders A : " + matchResultA.getMatchedPositions());
        Assertions.assertTrue(matchResultA.getMatchedPositions().get(0).
                getMatchedPositionAsPercentage()==100.0d);

        OverallMatchResult matchResultC = matchingService.getMatchedPositions("User C");
        System.out.println("Matched orders C : " + matchResultC.getMatchedPositions());
        Assertions.assertTrue(matchResultC.getMatchedPositions().get(0).
                getMatchedPositionAsPercentage()==25.0d);

    }


}

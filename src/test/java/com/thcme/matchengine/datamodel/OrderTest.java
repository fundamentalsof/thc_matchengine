package com.thcme.matchengine.datamodel;

//Junit test
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    public void testOrderCreation() {
        // Create an instance of Order
        Order order = new Order();
        order.setAmount(10.0d);
        order.setDirection(Direction.BUY);
        order.setUserId("user123");
        order.setValueDate(20200101);
        order.setDealtCurrency("USD");
        order.setCurrencyPair("EURUSD");
        System.out.println("Order created: " + order);

        Order order2 = new Order();
        order2.setAmount(10.0d);
        order2.setDirection(Direction.BUY);
        order2.setUserId("user123");
        order2.setValueDate(20200101);
        order2.setDealtCurrency("USD");
        order2.setCurrencyPair("EURUSD");
        Assertions.assertEquals(order, order2, "Orders with same attributes should be equal");
    }
}
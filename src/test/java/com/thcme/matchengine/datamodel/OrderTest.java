package com.thcme.matchengine.datamodel;

//Junit test
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    public void testOrderCreation() {
        // Create an instance of Order
        Order order = new Order("EURUSD", "USD",
                Order.Direction.BUY, 10.0d, 20200101, "user123");
        System.out.println("Order created: " + order);

        Order order2 = new Order("EURUSD", "USD",
                Order.Direction.BUY, 10.0d, 20200101, "user123");
        Assertions.assertEquals(order, order2, "Orders with same attributes should be equal");
    }
}
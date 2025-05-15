package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderSumissionServiceTest {
   
    @Autowired  
    IOrderSubmissionService orderSumissionService;
    
    @Test
    public void testFirstOrderSubmission() {
        // Test the order submission functionality
        
        Order newOrder = new Order(
                "EURUSD", "USD", 
                Order.Direction.BUY, 1000, 2, "user123");
        Order order = orderSumissionService.addOrder(newOrder);
        Assertions.assertEquals(newOrder, order, "First Order will be the same as " +
                "Aggregated Order");
        
    }
    
    
    
}

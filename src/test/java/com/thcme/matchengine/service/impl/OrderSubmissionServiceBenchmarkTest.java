package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.HdrHistogram.Histogram;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderSubmissionServiceBenchmarkTest {
    
    @Autowired
    private IOrderSubmissionService orderSubmissionService;
    
    Order orderA = new Order("EURUSD", "USD",
            Order.Direction.BUY, 10.0d, 20200101, "user123");
    Order orderB = new Order("EURUSD", "USD",
            Order.Direction.SELL, 10.0d, 20200101, "user123");
    
    
    // This test will be used to benchmark the order submission service.
    @Test
    public void testOrderSubmissionBenchmark() {

        Histogram histogram = new Histogram(3600000000000L, 3);
        // Assuming the order submission service has a method called submitOrder
        // This is a placeholder for the actual order submission logic.

        //Warmup...
        for (int i = 0; i < 100_000; i++) {
            orderSubmissionService.addOrder(orderA);
            orderSubmissionService.addOrder(orderB);
        }
        
        for (int i = 0; i < 1_000_000; i++) {
            long startNanoTime = System.nanoTime();    
            orderSubmissionService.addOrder(orderA);
            orderSubmissionService.addOrder(orderB);
            long endNanoTime = System.nanoTime() - startNanoTime;
            histogram.recordValue(endNanoTime);
        }

        System.out.println("HISTOGRAM Value at 999" + histogram.getValueAtPercentile(99.9d));
        histogram.outputPercentileDistribution(System.out, 1000.0);
        Assertions.assertTrue(
                histogram.getValueAtPercentile(99.9d)/1000 < 5, "999 value should be " +
                        "less than 5 Microseconds");
        
    }
}

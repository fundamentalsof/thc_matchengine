package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.interfaces.IMatchingService;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.HdrHistogram.Histogram;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MatchingServiceBenchmarkTest {
    @Autowired
    private IMatchingService matchingService;
    @Autowired
    private IOrderSubmissionService orderSubmissionService;
    @Autowired IOrderBookContextService orderBookContextService;
            

    Order orderA = new Order("EURUSD", "USD",
            Order.Direction.BUY, 10.0d, 20200101, "user123");
    Order orderB = new Order("EURUSD", "USD",
            Order.Direction.SELL, 10.0d, 20200101, "user456");



    @Test
    public void testMatchingBenchmark() {
        
        orderSubmissionService.addOrder(orderA);
        orderSubmissionService.addOrder(orderB);
        
        Histogram histogram = new Histogram(3600000000000L, 3);
        for (int i = 0; i < 100_000; i++) {
            matchingService.getMatchedPositions("user123");
        }
        orderBookContextService.reset();
        for (int i = 0; i < 1_000_000; i++) {
            orderSubmissionService.addOrder(orderA);
            orderSubmissionService.addOrder(orderB);

            long startTime = System.nanoTime();
            OverallMatchResult  result = matchingService.getMatchedPositions("user456");
            
            long endTime = System.nanoTime() - startTime;
            
            
            histogram.recordValue(endTime);
            printResult(result, false);
            
            orderBookContextService.reset();
            
        }
        System.out.println("HISTOGRAM Value at 999" + histogram.getValueAtPercentile(99.9d));
        histogram.outputPercentileDistribution(System.out, 1000.0);
        Assertions.assertTrue(
                histogram.getValueAtPercentile(99.9d)/1000 < 5, "999 value should be " +
                        "less than 5 Microseconds");
        
        
    }

    private void printResult(final OverallMatchResult result, boolean force) {
        if(force || System.getProperty("PRINT") != null) {
            System.out.println("Matched positions: " + result.getMatchedPositions());
        }
    }

}

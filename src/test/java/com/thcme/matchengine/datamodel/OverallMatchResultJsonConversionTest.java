package com.thcme.matchengine.datamodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OverallMatchResultJsonConversionTest {
    
    @Test
    public void testOverallMatchResultJsonConversion() {
        // Create an instance of OverallMatchResult

        OrderKey orderKey1 = new OrderKey("EURUSD", "USD", 2);
        OrderKey orderKey2 = new OrderKey("EURUSD", "EUR", 2);
        List<MatchResultPerOrderKey>  matchResultPerOrderKeys = List.of(
                new MatchResultPerOrderKey(
                        orderKey1.getCurrencyPair(), orderKey1.getDealtCurrency(), orderKey1.getValueDate(), 
                        "1000",
                        1.2345),
                new MatchResultPerOrderKey(orderKey2.getCurrencyPair(),
                        orderKey2.getDealtCurrency(), orderKey2.getValueDate(), "1000", 1.5678)
        );
        OverallMatchResult overallMatchResult = new OverallMatchResult(matchResultPerOrderKeys);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert the OverallMatchResult object to JSON
            String json = objectMapper.writeValueAsString(overallMatchResult);
            System.out.println("JSON: " + json);

            // Convert the JSON back to an OverallMatchResult object
            OverallMatchResult deserializedOverallMatchResult = objectMapper.readValue(json, OverallMatchResult.class);
            System.out.println("Deserialized OverallMatchResult: " + deserializedOverallMatchResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    
    
}

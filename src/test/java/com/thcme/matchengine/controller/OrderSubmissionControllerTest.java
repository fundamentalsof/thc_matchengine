package com.thcme.matchengine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc

public class OrderSubmissionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    String payload = """
                {
                 "currencyPair": "EURUSD",
                 "dealtCurrency": "USD",
                 "direction": "BUY",
                 "amount": "1000",
                 "valueDate": "20251023",
                 "userId": "User 1"
                }
            """; 
    
    @Test
    public void testOrderSubmission() throws Exception {
        // Perform a mock request to the order submission endpoint
        mockMvc.perform(post("/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(result -> result.getResponse().getContentAsString().contains("\"currencyPair\": \"EURUSD\""));
    }
    
}

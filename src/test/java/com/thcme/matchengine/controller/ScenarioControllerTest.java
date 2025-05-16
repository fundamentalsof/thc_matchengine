package com.thcme.matchengine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class ScenarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    String payloadA = """
                {
                 "currencyPair": "EURUSD",
                 "dealtCurrency": "USD",
                 "direction": "BUY",
                 "amount": "1000",
                 "valueDate": "20251023",
                 "userId": "UserA"
                }
            """;
    String payloadB = """
                {
                 "currencyPair": "EURUSD",
                 "dealtCurrency": "USD",
                 "direction": "SELL",
                 "amount": "1000",
                 "valueDate": "20251023",
                 "userId": "UserB"
                }
            """;

    @Test
    public void testScenarioController()  {
        try {
            mockMvc.perform(post("/addOrder")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payloadA))
                    .andExpect(result -> result.getResponse().getContentAsString().contains("\"currencyPair\": \"EURUSD\""));
            mockMvc.perform(post("/addOrder")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payloadB));

            mockMvc.perform(MockMvcRequestBuilders.get("/matchPositions/UserA"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("100.0")));

            mockMvc.perform(MockMvcRequestBuilders.get("/matchPositions/UserB"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("100.0")));
            
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

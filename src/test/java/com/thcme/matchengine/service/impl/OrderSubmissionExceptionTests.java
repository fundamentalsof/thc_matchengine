package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.controller.OrderSubmissionController;
import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderSubmissionExceptionTests{

    private MockMvc mockMvc;

    @Mock
    private IOrderSubmissionService orderSubmissionService;

    @InjectMocks
    private OrderSubmissionController orderSubmissionController;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(orderSubmissionController).build();
    }

    @Test
    public void testAddOrderException() throws Exception {
        // Configure the mock to throw an exception
        Mockito.when(orderSubmissionService.addOrder(any(Order.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

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

        // Perform the request and expect an internal server error
        mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError());
    }
}
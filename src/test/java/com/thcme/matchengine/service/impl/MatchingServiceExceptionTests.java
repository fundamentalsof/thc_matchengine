package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.controller.MatchingController;
import com.thcme.matchengine.controller.OrderSubmissionController;
import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.service.interfaces.IMatchingService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MatchingServiceExceptionTests {

    private MockMvc mockMvc;

    @Mock
    private IMatchingService matchingService;

    @InjectMocks
    private MatchingController matchingController;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(matchingController).build();
    }

    @Test
    public void testMatchPositionsException() throws Exception {
        // Configure the mock to throw an exception
        Mockito.when(matchingService.getMatchedPositions(any(String.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

        // Perform the request and expect an internal server error
        mockMvc.perform(get("/matchPositions/FOO"))
                .andExpect(status().isInternalServerError());
    }
}

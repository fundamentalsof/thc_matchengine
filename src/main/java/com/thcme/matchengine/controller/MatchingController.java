package com.thcme.matchengine.controller;

import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.impl.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingController {
    
    @Autowired
    private MatchingService matchingService;
    
    // Endpoint to handle order matching that takes User ID as a parameter
    @GetMapping("/matchPositions/{userId}")
    
    public ResponseEntity<OverallMatchResult> matchOrders(@PathVariable("userId") String userId) {
        try {
            // Call the matching service to perform order matching
            OverallMatchResult result = matchingService.getMatchedPositions(userId);
            return new ResponseEntity<>(result, HttpStatus.OK);    
        }
        catch (Exception e) {
            // Handle any exceptions that occur during order matching
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}

package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.interfaces.IMatchingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MatchingServiceTest {
    @Autowired IMatchingService matchingService;
    
    @Test
    public void testMatchingService() {
        OverallMatchResult matchResult = matchingService.getMatchedPositions("FOO");
        Assertions.assertNotNull(matchResult, "Match result should not be null");
        Assertions.assertEquals(0, matchResult.getMatchedPositions().size(), "Matched orders should be empty");
    }
}

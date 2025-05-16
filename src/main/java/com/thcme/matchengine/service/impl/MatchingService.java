package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.MatchResultPerOrderKey;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.interfaces.IMatchingService;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService implements IMatchingService {
    @Autowired IOrderBookContextService orderBookContextService;

    @Override public OverallMatchResult getMatchedPositions(final String userid) {
        
         List<OrderBookContext> contexts = orderBookContextService.
                 getOrderBookContextsForUserId(userid);
         if (contexts == null || contexts.isEmpty()) {
             List<MatchResultPerOrderKey> emptyList = List.of();
             return  new OverallMatchResult(emptyList);
         }
         // logic to match positions
        List<MatchResultPerOrderKey> emptyList = List.of();
        return  new OverallMatchResult(emptyList);
         
    }
}

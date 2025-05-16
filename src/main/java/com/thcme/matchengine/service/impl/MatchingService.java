package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.interfaces.IMatchingService;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingService implements IMatchingService {
    @Autowired IOrderBookContextService orderBookContextService;

    @Override public OverallMatchResult getMatchedPositions(final String userid) {
        return null;
    }
}

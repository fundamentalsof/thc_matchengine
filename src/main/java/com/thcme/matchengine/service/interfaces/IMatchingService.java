package com.thcme.matchengine.service.interfaces;

import com.thcme.matchengine.datamodel.OverallMatchResult;

public interface IMatchingService {
    OverallMatchResult getMatchedPositions(String userid);
}

package com.thcme.matchengine.service.impl;

import com.thcme.matchengine.datamodel.MatchResultPerOrderKey;
import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.datamodel.OrderBookContext;
import com.thcme.matchengine.datamodel.OverallMatchResult;
import com.thcme.matchengine.service.interfaces.IMatchingService;
import com.thcme.matchengine.service.interfaces.IOrderBookContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingService implements IMatchingService {
    @Autowired IOrderBookContextService orderBookContextService;

    @Override public OverallMatchResult getMatchedPositions(final String userid) {
        
        List<MatchResultPerOrderKey> matchedOrders = new ArrayList<>();
         List<OrderBookContext> contexts = orderBookContextService.
                 getOrderBookContextsForUserId(userid);
         if (contexts == null || contexts.isEmpty()) {
             List<MatchResultPerOrderKey> emptyList = List.of();
             return  new OverallMatchResult(emptyList);
         }
         // logic to match positions
        contexts.stream().forEach(context -> {
            double matchedOrdersOthers = 0.0;
            double matchedOrdersUser = 0.0;
            double supply = 0;
            double totalAppetite = 0;

            synchronized (context) {
                
                totalAppetite = context.getCumulativeAppetiteInclusive(userid);
                matchedOrdersOthers = context.getCumulativeAppetiteExclusive(userid);
                matchedOrdersUser = totalAppetite - matchedOrdersOthers;
                Order.Direction userOrderDirection = context.getUserDirection(userid);
                supply = userOrderDirection == Order.Direction.BUY 
                        ? context.getCumulativeSellAmount()
                        : context.getCumulativeBuyAmount();
            }
            
            if (supply == 0 || matchedOrdersOthers >= supply) {
                MatchResultPerOrderKey matchResultPerOrderKey = new MatchResultPerOrderKey(
                        context.getOrderKey().getCurrencyPair(),
                        context.getOrderKey().getDealtCurrency(),
                        context.getOrderKey().getValueDate(),
                        userid,
                        0
                );
                matchedOrders.add(matchResultPerOrderKey);
            }
            else {
                MatchResultPerOrderKey matchResultPerOrderKey = new MatchResultPerOrderKey(
                        context.getOrderKey().getCurrencyPair(),
                        context.getOrderKey().getDealtCurrency(),
                        context.getOrderKey().getValueDate(),
                        userid,
                        supply >= totalAppetite ? 100:
                                (supply-matchedOrdersOthers)*100/matchedOrdersUser
                );
                matchedOrders.add(matchResultPerOrderKey);
            }
        });
        OverallMatchResult overallMatchResult = new OverallMatchResult(matchedOrders);
        return overallMatchResult;
    }
}

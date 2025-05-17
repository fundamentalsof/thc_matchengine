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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
            AtomicReference<Double> matchedOrdersOthers = new AtomicReference<>(0.0);
            AtomicReference<Double> matchedOrdersUser = new AtomicReference<>(0.0);
            double supply = 0;
            double appetite = 0;

            synchronized (context) {

                boolean isBuy = context.isUserPresentInBuyMap(userid);
                List<Order> orders = isBuy
                        ? context.getBuyListAcrossAllParticipants()
                        : context.getSellListAcrossAllParticipants();

                for (Order order : orders) {
                    if (order.getUserId().equals(userid)) {
                        matchedOrdersUser.updateAndGet(v -> v + order.getAmount());
                        break;
                    } else {
                        matchedOrdersOthers.updateAndGet(v -> v + order.getAmount());
                    }
                }
                appetite = matchedOrdersUser.get() + matchedOrdersOthers.get();
                Map<String, Order> aggregatedOrders = isBuy
                        ? context.getSellMapOfUserIdsToAggregatedOrders()
                        : context.getBuyMapOfUserIdsToAggregatedOrders();

                for (Map.Entry<String, Order> entry : aggregatedOrders.entrySet()) {
                    if (entry.getKey().equals(userid)) {
                        continue;
                    }
                    Order order = entry.getValue();
                    if (order != null) {
                        supply += order.getAmount();
                    }
                    if (supply >= appetite) {
                        break;
                    }
                }
            }
            
            if (supply == 0 || matchedOrdersOthers.get() >= supply) {
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
                        supply >= appetite ? 100:
                                (supply-matchedOrdersOthers.get())*100/matchedOrdersUser.get()
                );
                matchedOrders.add(matchResultPerOrderKey);
            }
        });
        OverallMatchResult overallMatchResult = new OverallMatchResult(matchedOrders);
        return overallMatchResult;
    }
}

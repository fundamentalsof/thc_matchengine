package com.thcme.matchengine.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultPerOrderKey {
    private String currencyPair;
    private String dealtCurrency;
    private int valueDate;
    String userId;
    double  matchedPositionAsPercentage;
}

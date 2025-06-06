package com.thcme.matchengine.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class Order {
    private String currencyPair;
    private String dealtCurrency;
    private Direction direction;
    private double amount;
    private int valueDate;
    private String userId;

    public enum Direction {
        BUY,
        SELL;
    }

}


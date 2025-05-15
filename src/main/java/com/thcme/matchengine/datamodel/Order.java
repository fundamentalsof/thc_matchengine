package com.thcme.matchengine.datamodel;

import lombok.Data;

@Data
public class Order {
    private String currencyPair;
    private String dealtCurrency;
    private Direction direction;
    private double amount;
    private int valueDate;
    private String userId;
}

enum Direction {
    BUY,
    SELL
}

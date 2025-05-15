package com.thcme.matchengine.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class OrderKey {
    private String currencyPair;
    private String dealtCurrency;
    private int valueDate;
}

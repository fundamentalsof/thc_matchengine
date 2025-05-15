package com.thcme.matchengine.service;

import com.thcme.matchengine.datamodel.Order;

public interface IOrderSubmissionService {

    /**
     * This method is used to add an order to the system.
     * @param order
     * @return Aggregated / Netted Order for the user submitting the order
     */
    public Order addOrder(Order order);
}

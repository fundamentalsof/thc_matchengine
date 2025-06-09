package com.thcme.matchengine.stepdefs;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.service.interfaces.IOrderSubmissionService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class OrderSubmissionSteps {

    @Autowired
    private IOrderSubmissionService orderSubmissionService;

    private Order order;
    private Order submittedOrder;


    @Before
    public void setup() {
        // Reset the order submission service before each scenario
        orderSubmissionService.reset();
        order = null;
        submittedOrder = null;
    }

    @Given("^an order with direction (\\w+), amount ([0-9]+\\.?[0-9]*), and user (\\w+)$")
    public void givenOrder(String direction, double amount, String userId) {
        order = new Order("EURUSD", "USD", Order.Direction.valueOf(direction), amount, 2, userId);
    }

    @Given("an order with direction SELL, amount {double}, and user {string}")
    public void an_order_with_direction_sell_amount_and_user(Double amount, String userId){
        order = new Order("EURUSD", "USD", Order.Direction.SELL, amount, 2, userId);
    }
    @Given("an order with direction BUY, amount {double}, and user {string}")
    public void an_order_with_direction_buy_amount_and_user(Double amount, String userId){
        order = new Order("EURUSD", "USD", Order.Direction.BUY, amount, 2, userId);
    }

    @When("the order is submitted")
    public void whenOrderIsSubmitted() {
        submittedOrder = orderSubmissionService.addOrder(order);
    }

    @Then("the order should be added to the {string} side")
    public void thenOrderShouldBeAddedToSide(String side) {
        Assertions.assertEquals(Order.Direction.valueOf(side), submittedOrder.getDirection());
        Assertions.assertEquals(order, submittedOrder);
    }

    @Then("the order should be added to the SELL side")
    public void thenOrderShouldBeAddedToSideSELL() {
        Assertions.assertEquals(Order.Direction.SELL, submittedOrder.getDirection());
        //Assertions.assertEquals(order, submittedOrder);
    }
    @Then("the order should be added to the BUY side")
    public void thenOrderShouldBeAddedToSideBUY() {
        Assertions.assertEquals(Order.Direction.BUY, submittedOrder.getDirection());
//        Assertions.assertEquals(order, submittedOrder);
    }


}
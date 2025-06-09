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

    @Given("an order with direction {word}, amount {double}, and user {string}")
    public void an_order_with_direction_buy_sell_amount_and_user(String direction, Double amount,
                                                            String userId){
        order = new Order("EURUSD", "USD", 
                Order.Direction.valueOf(direction), amount, 2, userId);
    }

    @When("the order is submitted")
    public void whenOrderIsSubmitted() {
        submittedOrder = orderSubmissionService.addOrder(order);
    }

    @Then("the order should be added to the {word} side")
    public void thenOrderShouldBeAddedToSideBUY(String side) {
        Assertions.assertEquals(Order.Direction.valueOf(side), submittedOrder.getDirection());
//        Assertions.assertEquals(order, submittedOrder);
    }


}
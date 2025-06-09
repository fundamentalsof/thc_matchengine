Feature: Order Submission

  Scenario: Submit a SELL order
Given an order with direction SELL, amount 1000.0, and user "user123"
When the order is submitted
Then the order should be added to the SELL side
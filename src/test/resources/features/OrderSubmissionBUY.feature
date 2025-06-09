Feature: Order Submission

  Scenario: Submit a BUY order
    Given an order with direction BUY, amount 1000.0, and user "user123"
    When the order is submitted
    Then the order should be added to the BUY side

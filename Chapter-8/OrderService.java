package com.example;

import org.springframework.stereotype.Service;

class OrderFulfillmentMessage {
  private String message;

  public OrderFulfillmentMessage(Order order) {
    this.message = "Fulfill order ID " + order.getId() + " with amount " + order.getAmount();
  }

  public String getMessage() {
    return message;
  }
}

class OrderConfirmationMessage {
  private String message;

  public OrderConfirmationMessage(String string) {
    this.message = "Order " + string + " confirmed.";
  }

  public String getMessage() {
    return message;
  }
}

@Service
public class OrderService {

  private final MessageProducer messageProducer;

  /**
   * Constructor-based dependency injection for better testing and decoupling.
   * 
   * @param messageProducer The MessageProducer used to send messages.
   */
  public OrderService(MessageProducer messageProducer) {
    this.messageProducer = messageProducer;
  }

  /**
   * Processes an order by first checking the balance and then sending
   * confirmation messages.
   * 
   * @param order The order to process.
   * @throws InsufficientBalanceException If there is not enough balance for the
   *                                      order.
   */
  public void processOrder(Order order) throws InsufficientBalanceException {
    // Validate order and deduct balance
    deductBalance(order.getId(), order.getAmount());

    // Publish order confirmation message
    OrderConfirmationMessage confirmation = new OrderConfirmationMessage(order.getId());
    messageProducer.sendMessage(confirmation.getMessage());

    // Publish order fulfillment message
    publishFulfillmentMessage(order);
  }

  /**
   * Deducts the balance for a given order. Throws an exception if funds are
   * insufficient.
   */
  private void deductBalance(String string, double d) throws InsufficientBalanceException {
    // Placeholder for actual balance deduction logic
    throw new InsufficientBalanceException("Insufficient balance for order " + string);
  }

  private void publishFulfillmentMessage(Order order) {
    // Create the fulfillment message based on the order details
    OrderFulfillmentMessage fulfillmentMessage = new OrderFulfillmentMessage(order);

    // Send the message through the MessageProducer
    messageProducer.sendMessage(fulfillmentMessage.getMessage());
  }
}

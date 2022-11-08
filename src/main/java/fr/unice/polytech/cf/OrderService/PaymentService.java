package fr.unice.polytech.cf.OrderService;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Exceptions.TransactionFailureException;

import java.util.ArrayList;

public class PaymentService {
  private ArrayList<String> validCreditCards = new ArrayList<String>();

  public PaymentService() {
    validCreditCards.add("123456789");
  }

  public Receipt makePayment(String cardNumber, Order order) throws TransactionFailureException {
    boolean isCardValid = validCreditCards.stream().anyMatch(validCard -> validCard.equals(cardNumber));
    if(!isCardValid) throw new TransactionFailureException("Credit card not valid");
    return new Receipt(order);
  }
}

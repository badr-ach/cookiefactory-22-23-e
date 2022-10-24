package fr.unice.polytech.cf.OrderService;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import java.util.ArrayList;

public class PaymentService {
  private ArrayList<String> validCreditCards = new ArrayList<String>();

  public PaymentService() {
    validCreditCards.add("123456789");
    for (int i = 0; i < 10; i++) {
      String cardNumber = String.valueOf(i).repeat(9);
      validCreditCards.add(cardNumber);
    }
  }

  public Receipt makePayment(String cardNumber, Order order) throws RuntimeException {
    boolean isCardValid =
        validCreditCards.stream().anyMatch(validCard -> validCard.equals(cardNumber));
    if(!isCardValid) throw new RuntimeException("Credit card not valid");

    return new Receipt(order);
  }
}

package fr.unice.polytech.cf.OrderService;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;

public class PaymentService {
  public Receipt makePayment(String cardNumber, Order order) {
    double taxes = 10.0;
    ContactCoordinates contact = order.getContact();
    String adress = contact.getAdress();
    String name = contact.getName();
    double price = order.getPrice();
    order.setStatus(EOrderStatus.PAYED);
    return new Receipt(name, adress, price, taxes);
  }
}

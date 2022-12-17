package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.CustomerAccount;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Receipt;

public interface OrderFinalizer {
    Receipt makePayment(CustomerAccount customerAccount, String cardNumber, Order order);

    Receipt makePayment(ContactCoordinates contact, String cardNumber, Order order);
}

package fr.unice.polytech.cf.connectors;

import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Receipt;
import fr.unice.polytech.cf.exceptions.TransactionFailureException;

import java.util.ArrayList;

public class PaymentService {
    private ArrayList<String> validCreditCards = new ArrayList<String>();

    public PaymentService() {
        validCreditCards.add("123456789");
    }

    public Receipt makePayment(String cardNumber, Order order) throws TransactionFailureException {
        boolean isCardValid = validCreditCards.stream().anyMatch(validCard -> validCard.equals(cardNumber));
        if (!isCardValid) throw new TransactionFailureException("Credit card not valid");
        return new Receipt(order);
    }

    public Receipt makePayment(String cardNumber, ContactCoordinates customerContacts, Order order, double price) throws TransactionFailureException {
        boolean isCardValid = validCreditCards.stream().anyMatch(validCard -> validCard.equals(cardNumber));
        if (!isCardValid) throw new TransactionFailureException("Credit card not valid");
        return new Receipt(customerContacts, order);
    }
}

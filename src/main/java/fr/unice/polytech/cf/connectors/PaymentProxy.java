package fr.unice.polytech.cf.connectors;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Receipt;
import fr.unice.polytech.cf.exceptions.TransactionFailureException;
import fr.unice.polytech.cf.interfaces.Payment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PaymentProxy implements Payment {
    private ArrayList<String> validCreditCards = new ArrayList<String>();

    public PaymentProxy() {
        validCreditCards.add("123456789");
    }

    @Override
    public Receipt makePayment(String cardNumber, Order order) throws TransactionFailureException {
        boolean isCardValid = validCreditCards.stream().anyMatch(validCard -> validCard.equals(cardNumber));
        if (!isCardValid) throw new TransactionFailureException("Credit card not valid");
        return new Receipt(order);
    }
}

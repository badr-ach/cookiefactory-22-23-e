package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Receipt;
import fr.unice.polytech.cf.exceptions.TransactionFailureException;

public interface Payment {
    Receipt makePayment(String cardNumber, Order order) throws TransactionFailureException;
}

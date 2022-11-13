package fr.unice.polytech.cf;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.accountservice.exceptions.InvalidCredentialsException;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.orderservice.exceptions.InvalidRetrievalDateException;
import fr.unice.polytech.cf.storeservice.exceptions.InvalidStoreException;
import fr.unice.polytech.cf.storeservice.entities.Store;
import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.Receipt;


public class CustomerSystem extends CookieOnDemandSystem {

    private Order activeOrder;
    private Store activeStore;
    private Account loggedInAccount;
    private String coupon;

    public Order startOrder() {
        this.activeOrder = orderService.startOrder();
        return activeOrder;
    }

    public Order addCookie(Cookie cookie){
        return this.addCookie(cookie,1);
    }
    public Order addCookie(Cookie cookie, int quantity){
        return orderService.addCookies(activeOrder, new HashMap<>(Collections.singletonMap(cookie,quantity)));
    }


    public Receipt payOrder(ContactCoordinates coordinates, String cardNumber) {
        if (loggedInAccount != null) coordinates = loggedInAccount.getContactCoordinates();
        if (activeOrder.getStore() == null) throw new InvalidStoreException("Store not specified");
        if (activeOrder.getRetrievalDateTime() == null)
            throw new InvalidRetrievalDateException("Retrieval time not specified");
        double price = activeOrder.getPrice() + activeStore.getTaxes() * activeOrder.getPrice();
        return orderService.makePayment(coordinates, cardNumber, activeOrder, price);
    }

    public Receipt payOrder(Account account, String cardNumber) {
        ContactCoordinates contactCoordinates = account.getContactCoordinates();
        double price = activeOrder.getPrice() + activeStore.getTaxes() * activeOrder.getPrice();
        return orderService.makePayment(contactCoordinates, cardNumber, activeOrder, price);
    }

    public void selectPickUpDate(LocalDateTime date) {
        activeOrder.setRetrievalDateTime(date);
    }

    public void selectStore(Store store) {
        if (storeService.getStores().contains(store)) {
            this.activeStore = store;
            activeOrder.setStore(activeStore);
        } else {
            throw new InvalidStoreException("Invalid Store Selected : inexistent");
        }
    }

    public Account signup(String username, String password, ContactCoordinates contactCoordinates) {
        return accountService.createCustomerAccount(username, password, contactCoordinates);
    }

    public Account login(String username, String password) {
        loggedInAccount = accountService.getAccount(username, password).orElseThrow(InvalidCredentialsException::new);
        return loggedInAccount;
    }
}

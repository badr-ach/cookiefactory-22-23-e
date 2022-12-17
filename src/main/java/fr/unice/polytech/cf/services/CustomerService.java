package fr.unice.polytech.cf.services;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import fr.unice.polytech.cf.components.AccountRegistry;
import fr.unice.polytech.cf.components.CookieComponent;
import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.components.StoreComponent;
import fr.unice.polytech.cf.connectors.PaymentProxy;
import fr.unice.polytech.cf.entities.*;
import fr.unice.polytech.cf.exceptions.InvalidCredentialsException;
import fr.unice.polytech.cf.interfaces.*;
import fr.unice.polytech.cf.exceptions.InvalidRetrievalDateException;
import fr.unice.polytech.cf.exceptions.InvalidStoreException;
import fr.unice.polytech.cf.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends CookieOnDemandService {

    private String coupon;
    private Order activeOrder;
    private Store activeStore;
    private CustomerAccount loggedInAccount;
    private CustomerFinder customerFinder;
    private OrderCreator orderCreator;
    private OrderModifier orderModifier;
    private OrderFinalizer orderFinalizer;
    private CookieRecipeFinder cookieRecipeFinder;
    private StoreCookModifier storeCookModifier;


    @Autowired
    public CustomerService(AccountRegistry accountRegistry,
                            OrderCreator orderCreator,OrderModifier orderModifier,
                           OrderFinalizer orderFinalizer, CookieRecipeFinder cookieRecipeFinder, StoreCookModifier storeCookModifier,PaymentProxy  paymentProxy, CustomerFinder customerFinder) {
        super(accountRegistry,cookieRecipeFinder, paymentProxy);
        this.customerFinder = customerFinder;
        this.orderModifier = orderModifier;
        this.orderFinalizer = orderFinalizer;
        this.orderCreator = orderCreator;
        this.storeCookModifier = storeCookModifier;





    }

    public Order startOrder() {
        this.activeOrder = orderCreator.startOrder();
        return activeOrder;
    }

    public Order addCookie(IPastry cookie) {
        return this.addCookie(cookie, 1);
    }

    public Order addCookie(IPastry cookie, int quantity) {
        return orderModifier.addCookies(activeOrder, new HashMap<>(Collections.singletonMap(cookie, quantity)));
    }

    public void paymentGuards() {
        if (activeOrder.getStore() == null) throw new InvalidStoreException("Store not specified");
        if (activeOrder.getRetrievalDateTime() == null)
            throw new InvalidRetrievalDateException("Retrieval time not specified");
    }

    public Receipt payOrder(ContactCoordinates coordinates, String cardNumber) {
        paymentGuards();
        return orderFinalizer.makePayment(coordinates, cardNumber, activeOrder);
    }

    public Receipt payOrder(CustomerAccount customerAccount, String cardNumber) {
        paymentGuards();
        return orderFinalizer.makePayment(customerAccount, cardNumber, activeOrder);
    }

    public void selectPickUpDate(LocalDateTime date) {
        activeOrder.setRetrievalDateTime(date);
    }

    public void selectStore(Store store) {
        if (getStores().contains(store)) {
            this.activeStore = store;
            activeOrder.setStore(activeStore);
        } else {
            throw new InvalidStoreException("Invalid Store Selected : inexistent");
        }
    }

    public CustomerAccount signup(String username, String password, ContactCoordinates contactCoordinates) {
        return accountRegistry.createCustomerAccount(username, password, contactCoordinates);
    }

    public CustomerAccount login(String username, String password) {
        loggedInAccount = customerFinder.getCustomerAccount(username, password).orElseThrow(InvalidCredentialsException::new);
        return loggedInAccount;
    }
}

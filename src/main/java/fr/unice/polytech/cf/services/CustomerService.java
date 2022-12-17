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
        return orderCreator.startOrder();
    }

    public Order addCookie(IPastry cookie,Order activeOrder) {
        return this.addCookie(cookie, 1,activeOrder);
    }

    public Order addCookie(IPastry cookie, int quantity, Order activeOrder) {
        return orderModifier.addCookies(activeOrder, new HashMap<>(Collections.singletonMap(cookie, quantity)));
    }

    public void paymentGuards(Order activeOrder) {
        if (activeOrder.getStore() == null) throw new InvalidStoreException("Store not specified");
        if (activeOrder.getRetrievalDateTime() == null)
            throw new InvalidRetrievalDateException("Retrieval time not specified");
    }

    public synchronized Receipt payOrder(ContactCoordinates coordinates, String cardNumber,Order activeOrder) {
        paymentGuards(activeOrder);
        return orderFinalizer.makePayment(coordinates, cardNumber, activeOrder);
    }

    public synchronized Receipt payOrder(CustomerAccount customerAccount, String cardNumber,Order activeOrder) {
        paymentGuards(activeOrder);
        return orderFinalizer.makePayment(customerAccount, cardNumber, activeOrder);
    }

    public void selectPickUpDate(LocalDateTime date,Order activeOrder) {
        activeOrder.setRetrievalDateTime(date);
    }

    public void selectStore(Store store,Order activeOrder) {
        if (getStores().contains(store)) {
            activeOrder.setStore(store);
        } else {
            throw new InvalidStoreException("Invalid Store Selected : inexistent");
        }
    }

    public CustomerAccount signup(String username, String password, ContactCoordinates contactCoordinates) {
        return accountRegistry.createCustomerAccount(username, password, contactCoordinates);
    }

    public CustomerAccount login(String username, String password) {
        return customerFinder.getCustomerAccount(username, password).orElseThrow(InvalidCredentialsException::new);
    }
}

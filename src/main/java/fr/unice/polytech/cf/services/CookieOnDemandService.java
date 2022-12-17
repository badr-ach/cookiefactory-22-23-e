package fr.unice.polytech.cf.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.unice.polytech.cf.components.AccountRegistry;
import fr.unice.polytech.cf.components.CookieComponent;
import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.components.StoreComponent;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.connectors.PaymentProxy;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.interfaces.CookieRecipeFinder;
import fr.unice.polytech.cf.interfaces.OrderCreator;
import fr.unice.polytech.cf.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class CookieOnDemandService {
    public AccountRegistry accountRegistry;
    protected PaymentProxy paymentProxy;
    private CookieRecipeFinder cookieRecipeFinder;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    public CookieOnDemandService(AccountRegistry accountRegistry,
                                 CookieRecipeFinder cookieRecipeFinder,  PaymentProxy paymentProxy) {
        this.accountRegistry = accountRegistry;
        this.paymentProxy = paymentProxy;
        this.cookieRecipeFinder = cookieRecipeFinder;

    }

    public List<Cookie> getCookies() {
        return cookieRecipeFinder.getCookies();
    }

    public List<Store> getStores() {
        List<Store> result = new ArrayList<>();
        storeRepository.findAll().forEach(result::add);
        return result;
    }
}

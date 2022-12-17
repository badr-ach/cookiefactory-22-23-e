package fr.unice.polytech.cf.services;

import fr.unice.polytech.cf.components.AccountRegistry;
import fr.unice.polytech.cf.components.CookieComponent;
import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.components.StoreComponent;
import fr.unice.polytech.cf.connectors.PaymentProxy;
import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.exceptions.UnauthorizedException;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.interfaces.CookieRecipeFinder;
import fr.unice.polytech.cf.interfaces.CustomerFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackOfficeService extends CookieOnDemandService {
    @Autowired
    public BackOfficeService(AccountRegistry accountRegistry, CookieRecipeFinder cookieRecipeFinder, PaymentProxy paymentProxy) {
        super(accountRegistry, cookieRecipeFinder,paymentProxy);
    }



    public Store updateTaxes(Store store, double newTaxes, Account account) {
        if(account.getType() != EAccountType.STORE_MANAGER) throw new UnauthorizedException();
        store.setTaxes(newTaxes);
        return store;
    }
}


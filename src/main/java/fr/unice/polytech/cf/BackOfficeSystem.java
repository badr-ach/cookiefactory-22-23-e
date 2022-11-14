package fr.unice.polytech.cf;

import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.accountservice.exceptions.UnauthorizedException;
import fr.unice.polytech.cf.storeservice.entities.Store;

public class BackOfficeSystem extends CookieOnDemandSystem {

    public Store updateTaxes(Store store, double newTaxes, Account account) {
        if(account.getType() != EAccountType.STORE_MANAGER) throw new UnauthorizedException();
        store.setTaxes(newTaxes);
        return store;
    }
}


package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.exceptions.UnauthorizedException;
import fr.unice.polytech.cf.entities.Store;

public class BackOfficeSystem extends CookieOnDemandSystem {

    public Store updateTaxes(Store store, double newTaxes, Account account) {
        if(account.getType() != EAccountType.STORE_MANAGER) throw new UnauthorizedException();
        store.setTaxes(newTaxes);
        return store;
    }
}


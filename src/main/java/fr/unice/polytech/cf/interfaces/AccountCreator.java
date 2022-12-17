package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.CustomerAccount;
import fr.unice.polytech.cf.entities.enums.EAccountType;

public interface AccountCreator {
    Account createAccount(String username, String password, EAccountType accountType);

    CustomerAccount createCustomerAccount(
            String username, String password, ContactCoordinates contactCoordinates);
}

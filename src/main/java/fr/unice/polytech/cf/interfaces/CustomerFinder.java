package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.CustomerAccount;

import java.util.Optional;

public interface CustomerFinder {
    Optional<CustomerAccount> getCustomerAccount(String username, String password);

    Optional<Account> getAccount(String username, String password);

    boolean exists(String username);
}

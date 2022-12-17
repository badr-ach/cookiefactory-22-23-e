package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.CustomerAccount;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.exceptions.SignUpFailedException;
import java.util.Optional;

import fr.unice.polytech.cf.interfaces.AccountCreator;
import fr.unice.polytech.cf.interfaces.CustomerFinder;
import fr.unice.polytech.cf.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRegistry implements CustomerFinder, AccountCreator {

    private AccountRepository accountRepository;

    @Autowired
    public AccountRegistry(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Get an account by username and password
     * @param username
     * @param password
     * @return the account if it exists, an empty optional otherwise
     */
    @Override
    public Optional<Account> getAccount(String username, String password) {
        Iterable<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the username is already taken
     * @param username
     * @return a boolean that indicates if the username is already taken
     */
    @Override
    public boolean exists(String username) {
        Iterable<Account> allAccounts = accountRepository.findAll();
        for (Account account : allAccounts) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


    /**
     *  Retrieves the account of the customer with the given username and password
      * @param username
     * @param password
     * @return the account of the customer if it exists, an empty optional otherwise
     */
    @Override
    public Optional<CustomerAccount> getCustomerAccount(String username, String password) {
        Iterable<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            System.out.println("Account : " + account.getUsername() + " " + account.getPassword() +" " + account.getType() +", vs " + username+ ", " + password);
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                if (account.getType() == EAccountType.CUSTOMER) {
                    return Optional.of((CustomerAccount) account);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Create a new account with the given username, password and accountType
     * @param username
     * @param password
     * @param accountType
     * @return the created account
     */
    @Override
    public Account createAccount(String username, String password, EAccountType accountType) {
        if (exists(username)) throw new SignUpFailedException();
        Account account = new Account(username, password, accountType);
        accountRepository.save(account,account.getId());
        return account;
    }

    /**
     * Creates a customer account with the given username, password and contact coordinates
     * @param username
     * @param password
     * @param contactCoordinates
     * @return the created account
     */
    @Override
    public CustomerAccount createCustomerAccount(
            String username, String password, ContactCoordinates contactCoordinates) {
        if (exists(username)) throw new SignUpFailedException();
        CustomerAccount cutomerAccount = new CustomerAccount(username, password, contactCoordinates);
        accountRepository.save(cutomerAccount,cutomerAccount.getId());
        return cutomerAccount;
    }
}

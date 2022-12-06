package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.CustomerAccount;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.exceptions.SignUpFailedException;
import java.util.ArrayList;
import java.util.Optional;

public class AccountService {

  private ArrayList<Account> accounts = new ArrayList<Account>();
  private ArrayList<CustomerAccount> customerAccounts = new ArrayList<CustomerAccount>();

  public AccountService() {}

  public Optional<Account> getAccount(String username, String password) {
    return accounts.stream()
        .filter(
            account ->
                account.getUsername().equals(username) && account.getPassword().equals(password))
        .findFirst();
  }

  public Optional<CustomerAccount> getCustomerAccount(String username, String password) {
    return customerAccounts.stream()
        .filter(
            account ->
                account.getUsername().equals(username) && account.getPassword().equals(password))
        .findFirst();
  }

  public Account createAccount(String username, String password, EAccountType accountType) {
    if (exists(username)) throw new SignUpFailedException();
    Account account = new Account(username, password, accountType);
    accounts.add(account);
    return account;
  }

  public CustomerAccount createCustomerAccount(
      String username, String password, ContactCoordinates contactCoordinates) {
    if (exists(username)) throw new SignUpFailedException();
    CustomerAccount cutomerAccount = new CustomerAccount(username, password, contactCoordinates);
    accounts.add(cutomerAccount);
    customerAccounts.add(cutomerAccount);
    return cutomerAccount;
  }

  public boolean exists(String username) {
    return accounts.stream().anyMatch(account -> account.getUsername().equals(username));
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }
}

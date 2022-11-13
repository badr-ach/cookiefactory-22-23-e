package fr.unice.polytech.cf.accountservice;

import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.accountservice.entities.CutomerAccount;
import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.accountservice.exceptions.SignUpFailedException;
import java.util.ArrayList;
import java.util.Optional;

public class AccountService {

  private ArrayList<Account> accounts = new ArrayList<Account>();

  public AccountService() {}

  public Optional<Account> getAccount(String username, String password) {
    return accounts.stream()
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

  public Account createCustomerAccount(
      String username, String password, ContactCoordinates contactCoordinates) {
    if (exists(username)) throw new SignUpFailedException();
    CutomerAccount cutomerAccount = new CutomerAccount(username, password, contactCoordinates);
    accounts.add(cutomerAccount);
    return cutomerAccount;
  }

  public boolean exists(String username) {
    return accounts.stream().anyMatch(account -> account.getUsername().equals(username));
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }
}

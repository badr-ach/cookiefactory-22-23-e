package fr.unice.polytech.cf.AccountService;

import fr.unice.polytech.cf.AccountService.Entities.Account;

import java.util.ArrayList;

public class AccountService {

  private ArrayList<Account> accounts = new ArrayList<Account>();

  public AccountService() {}

  public Account getAccount(String username, String password) {
    return accounts.stream()
        .filter(account -> account.getUsername() == username && account.getPassword() == password)
        .findFirst()
        .orElseThrow();
  }


}

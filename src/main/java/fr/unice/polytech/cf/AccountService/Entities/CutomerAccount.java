package fr.unice.polytech.cf.AccountService.Entities;

import fr.unice.polytech.cf.AccountService.Enums.EAccountType;

public class CutomerAccount extends Account {

  public CutomerAccount(String username, String password, EAccountType type) {
    super(username, password, type);
  }
}

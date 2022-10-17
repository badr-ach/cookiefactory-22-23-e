package fr.unice.polytech.cf.AccountService.Entities;

import fr.unice.polytech.cf.AccountService.Enums.EAccountType;

public class Account {
  protected int id;
  protected String username;
  protected String password;
  protected EAccountType type;
  private int ID = 0;

  public Account(String username, String password, EAccountType type){
    this.password = password;
    this.username = username;
    this.type = type;
    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public EAccountType getType() {
    return type;
  }
}

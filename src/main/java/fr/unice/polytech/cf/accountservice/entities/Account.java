package fr.unice.polytech.cf.accountservice.entities;

import fr.unice.polytech.cf.accountservice.enums.EAccountType;

import java.util.List;

public class Account {
  protected int id;
  protected String username;
  protected String password;
  protected EAccountType type;
  protected ContactCoordinates contactCoordinates;
  private int ID = 0;

  public Account(String username, String password, EAccountType type){
    this.password = password;
    this.username = username;
    this.type = type;
    this.id = ID;
    ID++;
  }

  public Account(String username, String password, EAccountType type, ContactCoordinates contactCoordinates){
    this.password = password;
    this.username = username;
    this.contactCoordinates = contactCoordinates;
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

  public void setContactCoordinates(ContactCoordinates contactCoordinates) {
    this.contactCoordinates = contactCoordinates;
  }

  public ContactCoordinates getContactCoordinates() {
    return contactCoordinates;
  }
}

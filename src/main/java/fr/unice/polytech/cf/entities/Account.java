package fr.unice.polytech.cf.entities;

import fr.unice.polytech.cf.entities.enums.EAccountType;

import java.util.UUID;

public class Account {
  protected UUID id;
  protected String username;
  protected String password;
  protected EAccountType type;
  protected ContactCoordinates contactCoordinates;


  public Account(String username, String password, EAccountType type){
    this.password = password;
    this.username = username;
    this.type = type;
    this.id = UUID.randomUUID();

  }

  public Account(String username, String password, EAccountType type, ContactCoordinates contactCoordinates){
    this.password = password;
    this.username = username;
    this.contactCoordinates = contactCoordinates;
    this.type = type;
    this.id = UUID.randomUUID();

  }

  public UUID getId() {
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

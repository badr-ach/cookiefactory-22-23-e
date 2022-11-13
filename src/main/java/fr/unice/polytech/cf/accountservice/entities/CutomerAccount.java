package fr.unice.polytech.cf.accountservice.entities;

import fr.unice.polytech.cf.accountservice.enums.EAccountType;

public class CutomerAccount extends Account {

  public CutomerAccount(String username, String password, ContactCoordinates contactCoordinates) {
    super(username, password, EAccountType.CUSTOMER, contactCoordinates);
  }

}

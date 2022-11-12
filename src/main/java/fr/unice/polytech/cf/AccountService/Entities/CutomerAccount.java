package fr.unice.polytech.cf.AccountService.Entities;

import fr.unice.polytech.cf.AccountService.Enums.EAccountType;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import java.util.Stack;

public class CutomerAccount extends Account {

  public CutomerAccount(String username, String password, ContactCoordinates contactCoordinates) {
    super(username, password, EAccountType.CUSTOMER, contactCoordinates);
  }

}

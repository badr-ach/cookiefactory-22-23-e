package fr.unice.polytech.cf.entities;

import java.util.ArrayList;
import java.util.List;

public class ContactCoordinates {

  private String name;
  private String email;
  private String phoneNumber;
  private String address;
  private List<String> pendingNotifications = new ArrayList<String>();
  private List<String> sentNotifications = new ArrayList<String>();

  public ContactCoordinates(String name){
    this.name = name;
  }
  public ContactCoordinates(String name, String email){
    this.name = name;
    this.email = email;
  }

  public ContactCoordinates(String name, String email, String phoneNumber, String address) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void notifyCustomer(){
    sentNotifications.addAll(pendingNotifications);
    pendingNotifications = new ArrayList<String>();
  }

  public void addNotifications(String message){
    pendingNotifications.add(message);
  }
  public List<String> getSentNotifications(){
    return  sentNotifications;
  }
}

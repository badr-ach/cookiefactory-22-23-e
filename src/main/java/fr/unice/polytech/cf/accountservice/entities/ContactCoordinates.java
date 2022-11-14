package fr.unice.polytech.cf.accountservice.entities;

public class ContactCoordinates {

  private String name;
  private String email;
  private String phoneNumber;
  private String address;

  public ContactCoordinates(String name){
    this.name = name;
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
}

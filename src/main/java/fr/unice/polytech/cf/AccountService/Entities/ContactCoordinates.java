package fr.unice.polytech.cf.AccountService.Entities;

public class ContactCoordinates {

  private String name;
  private String email;
  private String phoneNumber;
  private String adress;

  public ContactCoordinates(String name){
    this.name = name;
  }
  public ContactCoordinates(String name, String email, String phoneNumber, String adress) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.adress = adress;
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

  public String getAdress() {
    return adress;
  }
}

package fr.unice.polytech.cf.accountservice.exceptions;

public class SignUpFailedException extends RuntimeException {
  public SignUpFailedException() {
    super("The account already exists!");
  }

  public SignUpFailedException(String message) {
    super(message);
  }
}

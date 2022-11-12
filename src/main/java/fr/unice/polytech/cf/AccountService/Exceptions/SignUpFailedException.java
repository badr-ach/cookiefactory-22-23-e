package fr.unice.polytech.cf.AccountService.Exceptions;

public class SignUpFailedException extends RuntimeException {
  public SignUpFailedException() {
    super("The account already exists!");
  }

  public SignUpFailedException(String message) {
    super(message);
  }
}

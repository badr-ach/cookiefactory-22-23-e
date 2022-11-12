package fr.unice.polytech.cf.AccountService.Exceptions;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Username or password is incorrect!");
  }

  public InvalidCredentialsException(String message) {
    super(message);
  }
}

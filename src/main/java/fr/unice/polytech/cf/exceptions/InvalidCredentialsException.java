package fr.unice.polytech.cf.exceptions;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Username or password is incorrect!");
  }

  public InvalidCredentialsException(String message) {
    super(message);
  }
}

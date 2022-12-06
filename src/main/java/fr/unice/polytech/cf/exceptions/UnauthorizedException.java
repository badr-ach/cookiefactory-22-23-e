package fr.unice.polytech.cf.exceptions;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException() {
    super("Action unauthorized");
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}

package fr.dopolytech.polyshop.inventory.exceptions;

public class CreateEventFailException extends RuntimeException {
  public CreateEventFailException(String eventString) {
    super("Fail to insert Event in DB " + eventString);
  }
}

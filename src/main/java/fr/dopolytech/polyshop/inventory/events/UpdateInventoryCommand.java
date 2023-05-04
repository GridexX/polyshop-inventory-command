package fr.dopolytech.polyshop.inventory.events;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdateInventoryCommand {
  String id;
  long quantity;
  LocalDateTime date;

  public UpdateInventoryCommand(String id, long quantity) {
    this.id = id;
    this.quantity = quantity;
    this.date = LocalDateTime.now();
  }
}


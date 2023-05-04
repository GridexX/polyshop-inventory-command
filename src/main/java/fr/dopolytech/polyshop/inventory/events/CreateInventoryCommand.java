package fr.dopolytech.polyshop.inventory.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class CreateInventoryCommand {
  String id;
  long quantity;
  LocalDateTime date;

  public CreateInventoryCommand(String id, long quantity) {
    this.id = id;
    this.quantity = quantity;
    this.date = LocalDateTime.now();
  }
}

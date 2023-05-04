package fr.dopolytech.polyshop.inventory.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InventoryUpdateEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;

  String productId;

  long quantity;

  @Column(name = "date", columnDefinition = "TIMESTAMP")
  LocalDateTime date;

  public InventoryUpdateEvent() {

  }

  public InventoryUpdateEvent(String productId, long quantity) {
    this.productId = productId;
    this.date = LocalDateTime.now();
    this.quantity = quantity;
  }

}

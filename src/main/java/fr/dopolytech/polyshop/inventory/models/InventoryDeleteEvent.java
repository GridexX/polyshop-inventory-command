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
public class InventoryDeleteEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;

  String productId;

  @Column(name = "date", columnDefinition = "TIMESTAMP")
  LocalDateTime date;


  public InventoryDeleteEvent() {

  }

  public InventoryDeleteEvent(String productId) {
    this.productId = productId;
    this.date = LocalDateTime.now();
  }
}

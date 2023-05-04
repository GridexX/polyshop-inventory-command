package fr.dopolytech.polyshop.inventory.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.dopolytech.polyshop.inventory.dto.InventoryDto;
import fr.dopolytech.polyshop.inventory.dto.PutInventoryDto;
import fr.dopolytech.polyshop.inventory.exceptions.CreateEventFailException;
import fr.dopolytech.polyshop.inventory.models.Inventory;
import fr.dopolytech.polyshop.inventory.repositories.InventoryRepository;
import fr.dopolytech.polyshop.inventory.services.InventoryService;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping(consumes = "application/json")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ResponseEntity<Object> addInventory(@RequestBody InventoryDto inventory) {
    try {
      InventoryDto invDto = inventoryService.save(inventory);
      return ResponseEntity.ok(invDto);
    } catch (CreateEventFailException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @PutMapping(value = "/{id}", consumes = "application/json")
  public ResponseEntity<Object> updateInventory(@PathVariable("id") String id,
      @RequestBody PutInventoryDto putInventoryDto) {
    try {

      InventoryDto inv = inventoryService.update(id, putInventoryDto);
      return ResponseEntity.ok(inv);
    } catch (CreateEventFailException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public ResponseEntity<Object> deleteInventory(@PathVariable("id") String id) {
    try {
      inventoryService.deleteByProductId(id);
      return ResponseEntity.noContent().build();
    } catch (CreateEventFailException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}

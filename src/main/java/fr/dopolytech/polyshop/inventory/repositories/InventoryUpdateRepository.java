package fr.dopolytech.polyshop.inventory.repositories;

import org.springframework.stereotype.Repository;

import fr.dopolytech.polyshop.inventory.models.InventoryUpdateEvent;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InventoryUpdateRepository extends JpaRepository<InventoryUpdateEvent, String> {
  
}

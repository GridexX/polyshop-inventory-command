package fr.dopolytech.polyshop.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dopolytech.polyshop.inventory.models.InventoryCreateEvent;

@Repository
public interface InventoryCreateRepository extends JpaRepository<InventoryCreateEvent, String> {

}

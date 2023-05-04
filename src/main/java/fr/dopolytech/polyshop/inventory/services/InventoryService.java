package fr.dopolytech.polyshop.inventory.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.inventory.dto.InventoryDto;
import fr.dopolytech.polyshop.inventory.dto.PutInventoryDto;
import fr.dopolytech.polyshop.inventory.events.CreateInventoryCommand;
import fr.dopolytech.polyshop.inventory.exceptions.CreateEventFailException;
import fr.dopolytech.polyshop.inventory.models.Inventory;
import fr.dopolytech.polyshop.inventory.models.InventoryCreateEvent;
import fr.dopolytech.polyshop.inventory.models.InventoryDeleteEvent;
import fr.dopolytech.polyshop.inventory.models.InventoryUpdateEvent;
import fr.dopolytech.polyshop.inventory.repositories.InventoryCreateRepository;
import fr.dopolytech.polyshop.inventory.repositories.InventoryDeleteRepository;
import fr.dopolytech.polyshop.inventory.repositories.InventoryRepository;
import fr.dopolytech.polyshop.inventory.repositories.InventoryUpdateRepository;

import org.springframework.amqp.core.Queue;

@Component
public class InventoryService {

  private final RabbitTemplate rabbitTemplate;
  private final InventoryRepository inventoryRepository;
  private final InventoryUpdateRepository inventoryUpdateRepository;
  private final InventoryDeleteRepository inventoryDeleteRepository;
  private final InventoryCreateRepository inventoryCreateRepository;
  private final Queue inventoryCommandUpdateQueue;
  private final Queue inventoryCommandCreateQueue;
  private final Queue inventoryCommandDeleteQueue;

  // Define the logger
  private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

  @Autowired
  public InventoryService(RabbitTemplate rabbitTemplate, Queue inventoryCommandUpdateQueue, Queue inventoryCommandCreateQueue, Queue inventoryCommandDeleteQueue,
      InventoryRepository inventoryRepository,
      InventoryCreateRepository inventoryCreateRepository,
      InventoryDeleteRepository inventoryDeleteRepository,
      InventoryUpdateRepository inventoryUpdateRepository
      ) {
    this.rabbitTemplate = rabbitTemplate;
    this.inventoryCommandUpdateQueue = inventoryCommandUpdateQueue;
    this.inventoryCommandDeleteQueue = inventoryCommandDeleteQueue;
    this.inventoryCommandCreateQueue = inventoryCommandCreateQueue;
    this.inventoryCreateRepository = inventoryCreateRepository;
    this.inventoryUpdateRepository = inventoryUpdateRepository;
    this.inventoryDeleteRepository = inventoryDeleteRepository;
    this.inventoryRepository = inventoryRepository;
  }

  public List<InventoryDto> findAll() {
    return inventoryRepository.findAll().stream().map(InventoryDto::new).collect(java.util.stream.Collectors.toList());
    // convert to dto

  }

  public InventoryDto findInventoryByProductId(String productId) {
    Inventory inv = inventoryRepository.findByProductId(productId);
    if(inv == null) {
      return null;
    }
    return new InventoryDto(inv);
  }

  public InventoryDto save(InventoryDto inventory) throws CreateEventFailException {
    logger.info("Save inventory {}", inventory);
    InventoryCreateEvent createEvent = new InventoryCreateEvent(inventory.id, inventory.quantity);
    InventoryCreateEvent savedEvent = inventoryCreateRepository.save(createEvent);
    if ( savedEvent == null ) {
      logger.error("Create event failed to be saved : {}", createEvent);
      throw new CreateEventFailException(createEvent.toString());
    }
    logger.info("Saved creation event {}", savedEvent);

    CreateInventoryCommand createCommand = new CreateInventoryCommand(inventory.id, inventory.quantity);

    ObjectMapper mapper = new ObjectMapper();
    try {
      String createCommandString =  mapper.writeValueAsString(createCommand);
      rabbitTemplate.convertAndSend(inventoryCommandCreateQueue.getName(), createCommandString);
    } catch (Exception e ) {
      logger.error("Failed to convert createCommand to JSON {}", e.getMessage());
      throw new CreateEventFailException(e.getMessage());
    }
    
    return inventory;
  }

  public InventoryDto update(String id, PutInventoryDto inventory) throws CreateEventFailException {
    logger.info("Update inventory {} with id {}", inventory, id);
    InventoryUpdateEvent updateEvent = new InventoryUpdateEvent(id, inventory.quantity);
    InventoryUpdateEvent savedEvent = inventoryUpdateRepository.save(updateEvent);
    if ( savedEvent == null ) {
      logger.error("Create event failed to be saved : {}", updateEvent);
      throw new CreateEventFailException(updateEvent.toString());
    }
    logger.info("Saved update  event {}", savedEvent);

    ObjectMapper mapper = new ObjectMapper();
    try {
      String updateCommandString =  mapper.writeValueAsString(updateEvent);
      rabbitTemplate.convertAndSend(inventoryCommandUpdateQueue.getName(), updateCommandString);
    } catch (Exception e ) {
      logger.error("Failed to convert updateCommand to JSON {}", e.getMessage());
      throw new CreateEventFailException(e.getMessage());
    }

    return new InventoryDto(id, inventory.quantity);
  }

  public void deleteByProductId(String productId) throws CreateEventFailException {
    logger.info("Delete inventory for id {}", productId);
    InventoryDeleteEvent deleteEvent = new InventoryDeleteEvent(productId);
    inventoryDeleteRepository.save(deleteEvent);

    ObjectMapper mapper = new ObjectMapper();
    try {
      String deleteCommandString =  mapper.writeValueAsString(deleteEvent);
      rabbitTemplate.convertAndSend(inventoryCommandDeleteQueue.getName(), deleteCommandString);
    } catch (Exception e ) {
      logger.error("Failed to convert deleteCommand to JSON {}", e.getMessage());
      throw new CreateEventFailException(e.getMessage());
    }

  }

}

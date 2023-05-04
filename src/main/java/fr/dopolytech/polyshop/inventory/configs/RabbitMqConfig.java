package fr.dopolytech.polyshop.inventory.configs;

import org.springframework.amqp.core.Queue;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.dopolytech.polyshop.inventory.services.InventoryService;


@Configuration
public class RabbitMqConfig {

  // Queue used to send message to the inventory_command service
  @Bean
  public Queue inventoryCommandUpdateQueue() {
    return new Queue("inventory_command_update", false);
  }

  @Bean
  public Queue inventoryCommandCreateQueue() {
    return new Queue("inventory_command_create", false);
  }
  @Bean
  public Queue inventoryCommandDeleteQueue() {
    return new Queue("inventory_command_delete", false);
  }

}
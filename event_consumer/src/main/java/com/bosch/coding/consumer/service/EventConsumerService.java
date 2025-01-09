package com.bosch.coding.consumer.service;

import com.bosch.coding.consumer.model.WarehouseRequestEvent;
import com.bosch.coding.consumer.repository.InventoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventConsumerService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @RabbitListener(queues = "warehouse.queue")
    public void processEvent(WarehouseRequestEvent event) {
        System.out.println("Received event: " + event);

        // Check if the inventory record for the fruit already exists
        Optional<WarehouseRequestEvent> optionalExistingEvent = inventoryRepository.findById(event.getFruit());
        if (optionalExistingEvent.isPresent()) {
            // Record exists, update quantity based on command
            WarehouseRequestEvent existingEvent = optionalExistingEvent.get();
            if ("add".equalsIgnoreCase(event.getCommand())) {
                // Add to inventory
                existingEvent.setQuantity(existingEvent.getQuantity() + event.getQuantity());
                System.out.println("Updated inventory (added): " + existingEvent);
            } else if ("remove".equalsIgnoreCase(event.getCommand())) {
                // Remove from inventory
                if (existingEvent.getQuantity() >= event.getQuantity()) {
                    existingEvent.setQuantity(existingEvent.getQuantity() - event.getQuantity());
                    System.out.println("Updated inventory (removed): " + existingEvent);
                } else {
                    System.out.println("Insufficient inventory to remove: " + event.getFruit());
                }
            }
            inventoryRepository.save(existingEvent); // Save the updated record
        } else {
            // Record does not exist, add it if the command is "add"
            if ("add".equalsIgnoreCase(event.getCommand())) {
                inventoryRepository.save(event); // Save the new record
                System.out.println("Added new inventory: " + event);
            } else {
                System.out.println("Cannot remove non-existent inventory for: " + event.getFruit());
            }
        }
    }
}
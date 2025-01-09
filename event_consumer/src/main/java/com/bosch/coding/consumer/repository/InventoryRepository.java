package com.bosch.coding.consumer.repository;

import com.bosch.coding.consumer.model.WarehouseRequestEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<WarehouseRequestEvent, String> {
    // Additional queries can be added here
}
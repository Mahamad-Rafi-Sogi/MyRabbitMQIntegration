package com.bosch.coding.consumer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class WarehouseRequestEvent {
    @Id
    private String fruit;
    private int quantity;

    @Transient // This field is only used for runtime operations and not stored in the database
    private String command;

    public WarehouseRequestEvent() {}

    public WarehouseRequestEvent(String fruit, int quantity, String command) {
        this.fruit = fruit;
        this.quantity = quantity;
        this.command = command;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "WarehouseRequestEvent{" +
                "fruit='" + fruit + '\'' +
                ", quantity=" + quantity +
                ", command='" + command + '\'' +
                '}';
    }
}
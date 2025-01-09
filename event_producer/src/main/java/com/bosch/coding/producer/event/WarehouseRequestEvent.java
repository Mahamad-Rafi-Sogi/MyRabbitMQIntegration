package com.bosch.coding.producer.event;

public class WarehouseRequestEvent {
    private final String fruit;
    private final Integer quantity;
    private final String command;

    public WarehouseRequestEvent(String fruit, int quantity, String command) {
        this.fruit = fruit;
        this.quantity = quantity;
        this.command = command;
    }

    public String getFruit() {
        return fruit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCommand() {
        return command;
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
package com.bosch.coding.producer.event;

import java.util.Random;

public class WarehouseRequestEventFactory {
    private static final String[] FRUITS = {"apples", "bananas", "coconuts", "dates", "elderberries"};
    private static final String[] COMMANDS = {"add", "remove"};
    private static final Random RANDOM = new Random();

    public WarehouseRequestEvent createEvent() {
        return new WarehouseRequestEvent(
                FRUITS[RANDOM.nextInt(FRUITS.length)],
                RANDOM.nextInt(10) + 1,
                COMMANDS[RANDOM.nextInt(COMMANDS.length)]
        );
    }
}
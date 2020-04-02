package pl.polsl.photoplus.model.enums;

import java.util.Arrays;

public enum OrderStatus {
    PENDING, PAID, READY_TO_SHIP, SHIPPED, DELIVERED;

    public static OrderStatus getOrderStatusFromString(final String name) throws IllegalArgumentException {
        if (name != null) {
            return Arrays.stream(OrderStatus.values())
                    .filter(x -> x.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown OrderStatus value: " + name));
        }
        return null;
    }
}

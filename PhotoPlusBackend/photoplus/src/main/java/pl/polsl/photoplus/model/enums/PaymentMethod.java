package pl.polsl.photoplus.model.enums;

import java.util.Arrays;

public enum PaymentMethod {
    CARD, CASH_ON_DELIVERY, PAYPAL;

    public static PaymentMethod getPaymentMethodFromString(final String name) throws IllegalArgumentException {
        if (name != null) {
            return Arrays.stream(PaymentMethod.values())
                    .filter(x -> x.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown OrderStatus value: " + name));
        }
        return null;
    }
}

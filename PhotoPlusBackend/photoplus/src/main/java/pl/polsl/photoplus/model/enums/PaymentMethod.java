package pl.polsl.photoplus.model.enums;

import pl.polsl.photoplus.model.exceptions.EnumValueException;

import java.util.Arrays;

public enum PaymentMethod {
    CARD, CASH_ON_DELIVERY, PAYPAL;

    public static PaymentMethod getPaymentMethodFromString(final String name) throws EnumValueException {
        if (name != null) {
            return Arrays.stream(PaymentMethod.values())
                    .filter(x -> x.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new EnumValueException("Unknown PaymentMethod value: " + name, PaymentMethod.class.getSimpleName()));
        }
        throw new EnumValueException("Unknown PaymentMethod value.", PaymentMethod.class.getSimpleName());
    }
}

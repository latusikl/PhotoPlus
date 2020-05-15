package pl.polsl.photoplus.converters;

import org.springframework.core.convert.converter.Converter;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.exceptions.EnumValueException;

public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(final String source) throws EnumValueException {
        return OrderStatus.getOrderStatusFromString(source);
    }

}
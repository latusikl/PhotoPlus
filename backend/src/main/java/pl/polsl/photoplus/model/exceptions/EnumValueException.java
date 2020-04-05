package pl.polsl.photoplus.model.exceptions;

import lombok.Getter;

public class EnumValueException extends RuntimeException {
    @Getter
    private String causeClassType;

    public EnumValueException(final String message, final String causeClassType) {
        super(message);
        this.causeClassType = causeClassType;
    }

}

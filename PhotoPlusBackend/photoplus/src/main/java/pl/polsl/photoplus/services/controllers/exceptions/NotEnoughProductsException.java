package pl.polsl.photoplus.services.controllers.exceptions;

import lombok.Getter;

public class NotEnoughProductsException extends RuntimeException {

    @Getter
    private String causeClassType;

    public NotEnoughProductsException(final String message, final String causeClassType)
    {
        super(message);
        this.causeClassType = causeClassType;
    }
}

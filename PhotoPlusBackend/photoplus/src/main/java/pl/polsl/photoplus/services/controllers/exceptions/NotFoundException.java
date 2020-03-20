package pl.polsl.photoplus.services.controllers.exceptions;

import lombok.Getter;

public class NotFoundException
        extends RuntimeException
{
    @Getter
    private String causeClassType;

    public NotFoundException(final String message, final String causeClassType)
    {
        super(message);
        this.causeClassType = causeClassType;
    }

}

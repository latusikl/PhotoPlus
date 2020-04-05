package pl.polsl.photoplus.services.controllers.exceptions;

import lombok.Getter;

public class PatchException extends RuntimeException
{
    @Getter
    private String causeClassType;

    public PatchException(final String message, final String causeClassType)
    {
        super(message);
        this.causeClassType = causeClassType;
    }

}

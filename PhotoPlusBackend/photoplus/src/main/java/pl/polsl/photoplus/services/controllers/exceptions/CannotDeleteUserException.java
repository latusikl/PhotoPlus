package pl.polsl.photoplus.services.controllers.exceptions;


import lombok.Getter;

public class CannotDeleteUserException extends RuntimeException
{
    @Getter
    private String causeClassType;

    public CannotDeleteUserException(final String message, final String causeClassType)
    {
        super(message);
        this.causeClassType = causeClassType;
    }
}

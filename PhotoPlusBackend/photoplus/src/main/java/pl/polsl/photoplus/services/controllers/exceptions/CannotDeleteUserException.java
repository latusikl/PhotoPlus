package pl.polsl.photoplus.services.controllers.exceptions;


public class CannotDeleteUserException extends Exception
{

    public CannotDeleteUserException(final String message)
    {
        super(message);
    }
}

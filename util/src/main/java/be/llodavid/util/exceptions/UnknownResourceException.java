package be.llodavid.util.exceptions;

public class UnknownResourceException extends OrderoException {

    public UnknownResourceException(String field, String resource) {
        super(String.format("The %s could not be found based on the provided %s.", field, resource));
    }
}
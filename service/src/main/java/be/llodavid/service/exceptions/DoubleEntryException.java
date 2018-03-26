package be.llodavid.service.exceptions;

public class DoubleEntryException extends OrderoException {
    public DoubleEntryException(String field, String entryData) {

        super(String.format("The %s %s is already present in the system.", field, entryData));
    }
}

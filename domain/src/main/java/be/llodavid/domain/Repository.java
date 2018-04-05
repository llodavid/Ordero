package be.llodavid.domain;

import be.llodavid.util.exceptions.OrderoException;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<E extends RepositoryRecord> {

    private Map<Integer, E> repository;
    private int idCounter = 1;

    public Repository() {
        repository = new HashMap<>();
    }

    public void injectDefaultData(List<E> defaultData)  {
        defaultData
                .forEach(record -> addRecord(record));
    }

    public E getRecordById(int recordId) {
        assertThatRecordExists(recordId);
        return repository.get(recordId);
    }

    public void assertThatRecordExists(int recordId) {
        if (!recordExists(recordId)) {
            throw new OrderoException("The record with ID: " + recordId + " couldn't be found.");
        }
    }

    public boolean recordExists(int recordId) {
        return repository.keySet().contains(recordId);
    }

    public List<E> getAllRecords() {
        return Collections.unmodifiableList(new ArrayList<>(repository.values()));
    }

    public E addRecord(E record) throws OrderoException {
        verifyIfRecordAlreadyExists(record);
        return addRecordToRepository(record);
    }

    private void verifyIfRecordAlreadyExists(E record) {
        if (recordAlreadyInRepository(record)) {
            throw new OrderoException(String.format("This %s record already exists in the database.", record.getClass().getSimpleName().toLowerCase()));
        }
    }

    public boolean recordAlreadyInRepository(E record) {
        return repository.values()
                .stream()
                .anyMatch(recordToCompare -> recordToCompare.equals(record));
    }

    private E addRecordToRepository(E record) {
        record.setId(idCounter);
        repository.put(idCounter, record);
        idCounter++;
        return record;
    }

    public E updateRecord(E record, int recordId) {
        assertThatRecordExists(recordId);
        record.setId(recordId); //To make sure nobody manipulates the recordId
        repository.put(recordId, record);
        return record;
    }

    public List<E> getRecordsForValueId(int valueId) {
        return repository.values().stream()
                .filter(value -> valueId == value.getId())
                .collect(Collectors.toList());
    }

    public List<E> getFilteredRecords (Predicate<E> filter) {
        return repository.values().stream()
                .filter(value -> filter.test(value))
                .collect(Collectors.toList());
    }
    public <F> List<F> filterRecordsWithFunction(Function<E, F> filter) {
        return repository.values().stream()
                .map(value -> filter.apply(value))
                .collect(Collectors.toList());
    }

    public void clear(){
        repository.clear();
        idCounter=0;
    }
}

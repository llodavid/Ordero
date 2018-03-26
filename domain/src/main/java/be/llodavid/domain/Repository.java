package be.llodavid.domain;

import javax.inject.Named;
import java.util.*;

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
            throw new IllegalArgumentException("The record with ID: " + recordId + " couldn't be found.");
        }
    }

    public boolean recordExists(int recordId) {
        return repository.keySet().contains(recordId);
    }

    public List<E> getAllRecords() {
        return Collections.unmodifiableList(new ArrayList<>(repository.values()));
    }

    public E addRecord(E record) throws IllegalArgumentException {
        if (recordAlreadyInRepository(record)) {
            throw new IllegalArgumentException("The record already exists.");
        }
        return addRecordToRepository(record);
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
    //        return value;
    //        }
    //            repository.put(valueID, value);
    //            value.setValueId(valueID);
    //        if (assertThatRecordExists(valueID)) {
    //    public Value updateValue(int valueID, Value value) {
    //    //NOT A REQUIREMENT
    //    }
    //        }
    //            repository.remove(valueId);
    //        if (assertThatRecordExists(valueId)) {
    //    public void removeValue(int valueId) {
//    //NOT A REQUIREMENT

//    }
}
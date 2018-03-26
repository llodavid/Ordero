package be.llodavid.domain;

import javax.inject.Named;
import java.util.*;

@Named
public abstract class GenericRepository<E extends RepositoryValue> {

    private Map<Integer, E> repository;
    private int idCounter = 1;

    public GenericRepository() {
        repository = new HashMap<>();
    }

    public void injectDefaultValueData(List<E> defaultData)  {
        defaultData
                .forEach(value -> addValue(value));
    }

    public E getValueById(int valueId) {
        assertThatValueExists(valueId);
        return repository.get(valueId);
    }

    public void assertThatValueExists(int valueId) {
        if (!valueExists(valueId)) {
            throw new IllegalArgumentException("No such" + repository.get(0).getClass().getSimpleName() +  "found.");
        }
    }

    public boolean valueExists(int valueId) {
        return repository.keySet().contains(valueId);
    }

    public List<E> getAllValues() {
        return Collections.unmodifiableList(new ArrayList<>(repository.values()));
    }

    public E addValue(E value) throws IllegalArgumentException {
        if (valueAlreadyInDatabase(value)) {
            throw new IllegalArgumentException("The value already exists.");
        }
        return addValueToDatabase(value);
    }

    public boolean valueAlreadyInDatabase(E value) {
        return repository.values()
                .stream()
                .anyMatch(valueToCompare -> valueToCompare.equals(value));
    }

    private E addValueToDatabase(E value) {
        value.setId(idCounter);
        repository.put(idCounter, value);
        idCounter++;
        return value;
    }
    //        return value;
    //        }
    //            repository.put(valueID, value);
    //            value.setValueId(valueID);
    //        if (assertThatValueExists(valueID)) {
    //    public Value updateValue(int valueID, Value value) {
    //    //NOT A REQUIREMENT
    //    }
    //        }
    //            repository.remove(valueId);
    //        if (assertThatValueExists(valueId)) {
    //    public void removeValue(int valueId) {
//    //NOT A REQUIREMENT

//    }
}

package be.llodavid.service;

import be.llodavid.domain.item.Item;
import be.llodavid.domain.item.ItemData;
import be.llodavid.domain.Repository;
import be.llodavid.service.exceptions.DoubleEntryException;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class ItemService {
    private Repository<Item> itemRepository;

    @Inject @Named("ItemRepo")
    public ItemService(Repository<Item> itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void injectDefaultData() {
        itemRepository.injectDefaultData(new ItemData().getDefaultItems());
    }

    public Item getItem(int itemID) throws UnknownResourceException {
        if (itemRepository.recordExists(itemID)) {
            return itemRepository.getRecordById(itemID);
        }
        throw new UnknownResourceException("item", "item ID: " + itemID);
    }

    public Item addItem(Item item) {
        if (!itemRepository.recordAlreadyInRepository(item)) {
            return itemRepository.addRecord(item);
        }
        throw new DoubleEntryException("item", String.format("%s", item.getName()));
    }

    public Item updateItem(Item item, int itemId) {
        if (itemRepository.recordExists(itemId)) {
            return itemRepository.updateRecord(item, itemId);
        }
        throw new UnknownResourceException("item", "item ID: " + itemId);
    }

    public List<Item> getAllItems() {
        return itemRepository.getAllRecords();
    }
}
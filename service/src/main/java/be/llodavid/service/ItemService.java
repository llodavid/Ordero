package be.llodavid.service;

import be.llodavid.domain.item.Item;
import be.llodavid.domain.item.ItemData;
import be.llodavid.domain.Repository;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.StockSupplyLevel;
import be.llodavid.service.exceptions.DoubleEntryException;
import be.llodavid.service.exceptions.UnknownResourceException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Named
public class ItemService {
    private Repository<Item> itemRepository;

    @Inject
    @Named("ItemRepo")
    public ItemService(Repository<Item> itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void injectDefaultData() {
        itemRepository.injectDefaultData(new ItemData().getDefaultItems());
    }

    public Item getItem(int itemId) throws UnknownResourceException {
        verifyIfItemExists(itemId);
        return itemRepository.getRecordById(itemId);
    }

    public Item addItem(Item item) {
        verifyItemDoesNotExistYet(item);
        return itemRepository.addRecord(item);
    }

    public Item updateItem(Item item, int itemId) {
        verifyIfItemExists(itemId);
        return itemRepository.updateRecord(item, itemId);
    }

    private void verifyIfItemExists(int itemId) {
        if (!itemRepository.recordExists(itemId)) {
            throw new UnknownResourceException("item", "item ID: " + itemId);
        }
    }

    private void verifyItemDoesNotExistYet(Item item) {
        if (itemRepository.recordAlreadyInRepository(item)) {
            throw new DoubleEntryException("item", String.format("%s", item.getName()));
        }
    }

    public List<Item> getAllItems() {
        return itemRepository.getAllRecords();
    }

    public Map<StockSupplyLevel, List<Item>> getItemsGroupedOnStockResupplyUrgency (){
        return this.getItemsGroupedOnStockResupplyUrgency(null);
    }

    public Map<StockSupplyLevel, List<Item>> getItemsGroupedOnStockResupplyUrgency (StockSupplyLevel stockSupplyLevel){
        return getAllItems().stream()
                .filter(stockResupplyUrgency(stockSupplyLevel))
                .collect(Collectors.groupingBy(
                        item-> stockLevel(item)));
    }

    private Predicate<Item> stockResupplyUrgency(StockSupplyLevel stockSupplyLevel) {
        return stockSupplyLevel == null ?
                item -> true :
                item-> stockLevel(item).equals(stockSupplyLevel);
    }

    private StockSupplyLevel stockLevel(Item item) {
        return StockSupplyLevel.getStockSupplyLevel(item.getStock());
    }

    public ItemGroup createItemGroup(int itemId, int amount) {
        return new ItemGroup(getItem(itemId), amount);
    }

    public void modifyStock(List<ItemGroup> orderItems) {
        orderItems.stream()
                .forEach(orderItem -> modifyStock(orderItem));
    }

    private void modifyStock(ItemGroup orderItem) {
        Item item = itemRepository.getRecordById(orderItem.getItemId());
        item.decreaseStock(orderItem.getAmount());
        itemRepository.updateRecord(item, item.getId());
    }
}
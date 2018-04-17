package be.llodavid.service;

import be.llodavid.domain.items.Item;
import be.llodavid.domain.items.ItemData;
import be.llodavid.domain.OrderoRepository;
import be.llodavid.domain.items.ItemRepository;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.StockSupplyLevel;
import be.llodavid.util.exceptions.DoubleEntryException;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ItemService {
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item getItem(long itemId) throws UnknownResourceException {
        verifyIfItemExists(itemId);
        return itemRepository.findById(itemId).get();
    }

    public Item addItem(Item item) {
        verifyItemDoesNotExistYet(item);
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        verifyIfItemExists(item.getId());
        return itemRepository.save(item);
    }

    private void verifyIfItemExists(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new UnknownResourceException("items", "items ID: " + itemId);
        }
    }

    private void verifyItemDoesNotExistYet(Item item) {
        if (itemRepository.existsById(item.getId())) {
            throw new DoubleEntryException("items", String.format("%s", item.getName()));
        }
    }

    public List<Item> getAllItems() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public SortedMap<StockSupplyLevel, List<Item>> getItemsGroupedOnStockResupplyUrgency() {
        return this.getItemsGroupedOnStockResupplyUrgency(null);
    }

    public SortedMap<StockSupplyLevel, List<Item>> getItemsGroupedOnStockResupplyUrgency(StockSupplyLevel stockSupplyLevel) {
        //TODO: do this with a query ?
        return getAllItems().stream()
                .filter(stockResupplyUrgency(stockSupplyLevel))
                .sorted(Comparator.comparing(Item::getStock))
                .collect(Collectors.groupingBy(
                        item -> stockLevel(item), TreeMap::new, Collectors.toList()));
    }

    private Predicate<Item> stockResupplyUrgency(StockSupplyLevel stockSupplyLevel) {
        return stockSupplyLevel == null ?
                item -> true :
                item -> stockLevel(item).equals(stockSupplyLevel);
    }

    private StockSupplyLevel stockLevel(Item item) {
        return StockSupplyLevel.getStockSupplyLevel(item.getStock());
    }

    public ItemGroup createItemGroup(long itemId, int amount) {
        return new ItemGroup(getItem(itemId), amount);
    }

    public void modifyStock(List<ItemGroup> orderItems) {
        orderItems.stream()
                .forEach(orderItem -> modifyStock(orderItem));
    }

    private void modifyStock(ItemGroup orderItem) {
        Item item = itemRepository.findById(orderItem.getItemId())
                .orElseThrow(()->new UnknownResourceException("item", "items ID: " + orderItem.getItemId()));
        item.decreaseStock(orderItem.getAmount());
        this.updateItem(item);
    }
}
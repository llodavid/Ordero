package be.llodavid.domain.items;

import be.llodavid.domain.RepositoryRecord;
import be.llodavid.util.exceptions.OrderoException;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

public class Item implements RepositoryRecord {
    private int itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock, backOrderedItems;

    private Item(ItemBuilder itemBuilder) {
        this.name = itemBuilder.name;
        this.description = itemBuilder.description;
        this.price = itemBuilder.price;
        verifyPriceIsPositive(price);
        this.stock = itemBuilder.stock;
        verifyStockIsPositive(stock);
    }

    private void verifyPriceIsPositive(BigDecimal price) {
        if (price.compareTo(ZERO) < 0) {
            throw new OrderoException("Price has to be positive");
        }
    }

    private void verifyStockIsPositive(int stock) {
        if (stock < 0) {
            throw new OrderoException("Stock cannot be negative.");
        }
    }

    @Override
    public void setId(int valueId) {
        this.itemId = valueId;
    }

    @Override
    public int getId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public void addItemsToStock(int nrOfItems) {
        verifyIfItemsToAddIsEqualOrGreaterThanZero(nrOfItems);
        this.stock += nrOfItems;
    }

    private void verifyIfItemsToAddIsEqualOrGreaterThanZero(int nrOfItems) {
        if (nrOfItems <= 0) {
            throw new OrderoException("Adding extra items has to be positive.");
        }
    }

    public void correctStock(int stock) {
        verifyStockIsPositive(stock);
        this.stock = 0;
        addItemsToStock(stock);
    }

    public void decreaseStock(int amount) {
        verifyStockIsPositive(amount);
        if (amount <= stock) {
            stock -= amount;
        } else {
            backOrderedItems += amount - stock;
            stock = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }


    public static class ItemBuilder {
        private String name;
        private String description;
        private BigDecimal price;
        private int stock;

        public static ItemBuilder buildItem() {
            return new ItemBuilder();
        }

        public Item build() {
            if (allFielsSet()) {
                return new Item(this);
            } else {
                throw new OrderoException("Please provide all the necessary arguments for the items.");
            }
        }

        private boolean allFielsSet() {
            return isFilledIn(name)
                    && isFilledIn(description)
                    && price != null;
        }

        private boolean isFilledIn(String field) {
            return field != null && !field.trim().equals("");
        }

        public ItemBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;

        }

        public ItemBuilder withStock(int stock) {
            this.stock = stock;
            return this;
        }
    }
}

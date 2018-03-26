package be.llodavid.domain.item;

import be.llodavid.domain.RepositoryRecord;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

public class Item implements RepositoryRecord {
    private int itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;

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
            throw new IllegalArgumentException("Price has to be positive");
        }
    }

    private void verifyStockIsPositive(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
    }

    public void setId(int valueId) {
        this.itemId = valueId;
    }

    public int getItemId() {
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
        if (nrOfItems <= 0) {
            throw new IllegalArgumentException("Adding extra items has to be positive.");
        }
        this.stock += nrOfItems;
    }

    public void correctStock(int stock) {
        verifyStockIsPositive(stock);
        this.stock = 0;
        addItemsToStock(stock);
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
                throw new IllegalArgumentException("Please provide all the necessary arguments for the item.");
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

package be.llodavid.domain.items;

import be.llodavid.domain.RepositoryRecord;
import be.llodavid.util.exceptions.OrderoException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

@Entity
@Table (name="ITEMS")
public class Item {

    @Id
    @SequenceGenerator(name = "items_generator", sequenceName = "items_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_generator")
    @Column(name = "ITEM_ID")
    private long itemId;
    @Column(name = "ITEM_NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name="STOCK")
    private int stock;
    @Column(name="BACKORDERED_ITEMS")
    private int backOrderedItems;

    private Item() {
    }

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

    public void setId(long valueId) {
        this.itemId = valueId;
    }

    public long getId() {
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

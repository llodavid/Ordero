package be.llodavid.domain.order;

import be.llodavid.domain.item.Item;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.BigDecimal.*;

public class ItemGroup {
    private int itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private int amount, shippingDays;
    private LocalDate shippingDate;
    private static final int NEXT_DAY_DELIVERY = 1;
    private static final int NEXT_WEEK_DELIVERY = 7;

    //Todo: ask Niels: should I Just create a new Item object that copies the content of old Item and use it in this class or is it more clear this way?
    public ItemGroup(Item item, int amount) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        verifyPriceIsPositive(item.getPrice());
        this.amount = amount;
        verifyAmountIsPositive(amount);
        this.shippingDays = calculateShippingDaysBasedOnStock(item.getStock());
    }

    private void verifyPriceIsPositive(BigDecimal price) {
        if (price.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Price has to be positive");
        }
    }

    private void verifyAmountIsPositive(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount ordered has to be positive");
        }
    }

    public void calculateShippingDate(LocalDate orderDate) {
        shippingDate = orderDate.plusDays(shippingDays);
    }

    private int calculateShippingDaysBasedOnStock(int stock) {
        return amount <= stock ? NEXT_DAY_DELIVERY : NEXT_WEEK_DELIVERY;
    }

    public BigDecimal calculateItemGroupTotal() {
        return price.multiply(new BigDecimal(amount));
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

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public int getShippingDays() {
        return shippingDays;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    @Override
    public String toString() {
        return "ItemGroup{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", shippingDate=" + shippingDate +
                '}';
    }
}

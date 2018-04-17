package be.llodavid.domain.orders;

import be.llodavid.domain.items.Item;
import be.llodavid.util.exceptions.OrderoException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.BigDecimal.*;

@Entity
@Table(name="ITEMGROUPS")
public class ItemGroup {

    @Id
    @SequenceGenerator(name = "Itemgroup_generator", sequenceName = "ITEMGROUP_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Itemgroup_generator")
    @Column(name = "ITEMGROUP_ID")
    private long itemGroupId;
    @Column(name = "FK_ITEM_ID")
    private long itemId;
    @Column(name = "ITEM_GROUP_NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "AMOUNT")
    private int amount;
    @Transient
    private int shippingDays;
    @Column(name = "SHIPPINGDATE")
    private LocalDate shippingDate;
    private static final int NEXT_DAY_DELIVERY = 1;
    private static final int NEXT_WEEK_DELIVERY = 7;

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

    private ItemGroup() {
    }

    private void verifyPriceIsPositive(BigDecimal price) {
        if (price.compareTo(ZERO) < 0) {
            throw new OrderoException("Price has to be positive");
        }
    }

    private void verifyAmountIsPositive(int amount) {
        if (amount < 1) {
            throw new OrderoException("Amount ordered has to be positive");
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

    public long getItemGroupId() {
        return itemGroupId;
    }

    public long getItemId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGroup itemGroup = (ItemGroup) o;
        return itemId == itemGroup.itemId &&
                amount == itemGroup.amount &&
                Objects.equals(name, itemGroup.name) &&
                Objects.equals(price, itemGroup.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, name, price, amount);
    }
}

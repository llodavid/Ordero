package be.llodavid.api.shoppingApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemGroupDTO {
    public int itemId;
    public String name;
    public String description;
    public BigDecimal price, itemGroupTotal;
    public int amount, shippingDays;
    public String shippingDate;

    public ItemGroupDTO withItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }

    public ItemGroupDTO withName(String name) {
        this.name = name;
        return this;
    }

    public ItemGroupDTO withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemGroupDTO withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ItemGroupDTO withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemGroupDTO withItemGroupTotal(BigDecimal itemGroupTotal) {
        this.itemGroupTotal = itemGroupTotal;
        return this;
    }
    public ItemGroupDTO withShippingDays(int shippingDays) {
        this.shippingDays = shippingDays;
        return this;
    }

    public ItemGroupDTO withShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGroupDTO that = (ItemGroupDTO) o;
        return itemId == that.itemId &&
                amount == that.amount &&
                shippingDays == that.shippingDays &&
                Objects.equals(price, that.price) &&
                Objects.equals(itemGroupTotal, that.itemGroupTotal) &&
                Objects.equals(shippingDate, that.shippingDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemId, price, itemGroupTotal, amount, shippingDays, shippingDate);
    }
}

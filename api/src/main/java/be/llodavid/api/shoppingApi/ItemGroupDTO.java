package be.llodavid.api.shoppingApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemGroupDTO {
    public int itemId;
    public String name;
    public String description;
    public BigDecimal price;
    public int amount, shippingDays;
    public LocalDate shippingDate;

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

    public ItemGroupDTO withShippingDays(int shippingDays) {
        this.shippingDays = shippingDays;
        return this;
    }

    public ItemGroupDTO withShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }
}

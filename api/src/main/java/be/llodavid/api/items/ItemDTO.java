package be.llodavid.api.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {
    public int itemId;
    public String name;
    public String description;
    public BigDecimal price;
    public int stock;

    public ItemDTO withItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }

    public ItemDTO withName(String name) {
        this.name = name;
        return this;
    }
    public ItemDTO withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemDTO withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ItemDTO withStock(int stock) {
        this.stock = stock;
        return this;
    }
}

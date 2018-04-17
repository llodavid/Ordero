package be.llodavid.api.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {
    public long itemId;
    public String name;
    public String description;
    public BigDecimal price;
    public int stock;

    public ItemDTO withItemId(long itemId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return itemId == itemDTO.itemId &&
                stock == itemDTO.stock &&
                Objects.equals(name, itemDTO.name) &&
                Objects.equals(description, itemDTO.description) &&
                Objects.equals(price, itemDTO.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemId, name, description, price, stock);
    }
}

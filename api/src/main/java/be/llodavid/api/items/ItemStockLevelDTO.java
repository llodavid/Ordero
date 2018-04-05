package be.llodavid.api.items;

import be.llodavid.domain.orders.StockSupplyLevel;

import java.util.List;

public class ItemStockLevelDTO {
    public String stockSupplyLevel;
    public List<ItemDTO> itemDTOS;

    public ItemStockLevelDTO withStockSupplyLevel(String stockSupplyLevel) {
        this.stockSupplyLevel = stockSupplyLevel;
        return this;
    }

    public ItemStockLevelDTO withItemDTOS(List<ItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
        return this;
    }
}

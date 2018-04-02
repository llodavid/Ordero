package be.llodavid.api.itemApi;

import be.llodavid.domain.order.StockSupplyLevel;

import java.util.List;

public class ItemStockLevelDTO {
    public StockSupplyLevel stockSupplyLevel;
    public List<ItemDTO> itemDTOS;

    public ItemStockLevelDTO withStockSupplyLevel(StockSupplyLevel stockSupplyLevel) {
        this.stockSupplyLevel = stockSupplyLevel;
        return this;
    }

    public ItemStockLevelDTO withItemDTOS(List<ItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
        return this;
    }
}

package be.llodavid.domain.orders;

public enum StockSupplyLevel {
    STOCK_LOW (5),
    STOCK_MEDIUM (10),
    STOCK_HIGH (Integer.MAX_VALUE);

    private int stockValue;

    StockSupplyLevel(int stockValue) {
        this.stockValue=stockValue;
    }

    public static StockSupplyLevel getStockSupplyLevel(int stock) {
        for (StockSupplyLevel stockUrgencyIndicator :
                StockSupplyLevel.values()) {
            if (stock < stockUrgencyIndicator.stockValue) {
                return stockUrgencyIndicator;
            }
        }
        return STOCK_HIGH;
    }

    public static StockSupplyLevel parse(String stockLevel) {
        try {
            return Enum.valueOf(StockSupplyLevel.class, stockLevel);
        } catch (Exception e) {
            return null;
        }
    }

    public int getStockValue() {
        return stockValue;
    }
}

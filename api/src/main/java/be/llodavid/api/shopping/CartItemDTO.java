package be.llodavid.api.shopping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemDTO {
    public int customerId;
    public int amount;
    public int itemId;
}

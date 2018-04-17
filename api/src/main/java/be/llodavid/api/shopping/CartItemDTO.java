package be.llodavid.api.shopping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemDTO {
    public long customerId;
    public int amount;
    public long itemId;
}

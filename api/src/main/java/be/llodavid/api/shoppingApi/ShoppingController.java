package be.llodavid.api.shoppingApi;

import be.llodavid.api.orderApi.OrderDTO;
import be.llodavid.api.orderApi.OrderMapper;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.service.ItemService;
import be.llodavid.service.OrderService;
import be.llodavid.service.ShoppingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ShoppingController {
    private ShoppingService shoppingService;
    private ItemGroupMapper itemGroupMapper;
    private ItemService itemService;
    private OrderMapper orderMapper;

    @Inject
    public ShoppingController(ShoppingService shoppingService, ItemGroupMapper itemGroupMapper, ItemService itemService, OrderMapper orderMapper) {
        this.shoppingService = shoppingService;
        this.itemGroupMapper = itemGroupMapper;
        this.itemService = itemService;
        this.orderMapper = orderMapper;
    }

    @GetMapping(path = "shoppingcart/{customerId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemGroupDTO> getShoppingCartContent(@PathVariable int customerId) {
        return shoppingService.getShoppingCartContent(customerId).stream()
                .map(itemGroup -> itemGroupMapper.ItemGroupToDTO(itemGroup))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/{customerId}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@PathVariable int customerId) {
        return orderMapper.orderToDTO(
                shoppingService.createOrder(customerId));
    }
    @PostMapping(path = "orders/{customerId}/{orderdid}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO reOrder(@PathVariable int customerId, @PathVariable int orderId) {
        return orderMapper.orderToDTO(
                shoppingService.reOrder(customerId,orderId));
    }

    //TODO ask niels how to best handle something like this. Is this ok? Is there a better way?
    @PostMapping(path="product/{itemId}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemGroupDTO addItemToShoppingCart(@PathVariable int itemId, @RequestBody CartItemDTO cartItemDTO) {
        return itemGroupMapper.ItemGroupToDTO(
                shoppingService.addItemToCart(itemId, cartItemDTO.amount)
                        , cartItemDTO.customerId);
    }
}

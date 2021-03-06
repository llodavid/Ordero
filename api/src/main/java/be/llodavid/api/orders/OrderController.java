package be.llodavid.api.orders;

import be.llodavid.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private OrderMapper orderMapper;

    // private ItemService itemService;
    @Inject
    public OrderController(OrderService orderService,
                           OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        // this.itemService = itemService;
        orderService.injectDefaultData();
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
                .map(order -> orderMapper.orderToDTO(order))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{orderId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrder(@PathVariable int orderId) {
        return orderMapper.orderToDTO(orderService.getOrder(orderId));
    }

    @GetMapping(path = "customers/{customerId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrdersForCustomer(@PathVariable int customerId) {
        return orderService.getAllOrdersForCustomer(customerId)
                .stream()
                .map(order -> orderMapper.orderToDTO(order))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "shipping/today", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsShippingTodayDTO> viewOrderItemsShippingToday() {
        return orderService.viewOrderItemsShippingToday()
                .entrySet().stream()
                .map(itemsShippingToday -> orderMapper.toItemsShippingTodayDTO(itemsShippingToday))
                .collect(Collectors.toList());
    }
}

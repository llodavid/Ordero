package be.llodavid.api.orderApi;

import be.llodavid.service.ItemService;
import be.llodavid.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.math.BigDecimal;
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

    @PostMapping(path = "/{customerId}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@PathVariable int customerId) {
        return orderMapper.orderToDTO(
                orderService.createOrderFromShoppingCart(customerId));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getOrders(){
        return orderService.getAllOrders()
                .stream()
                .map(order->orderMapper.orderToDTO(order))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{orderId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrder(@PathVariable int orderId) {
        return orderMapper.orderToDTO(orderService.getOrder(orderId));
    }

    private BigDecimal getAmount(String paidAmount) {
        //TODO Test this and look for a better way
        try {
            return new BigDecimal(paidAmount);
        } catch (Exception e) {
            throw new RuntimeException("ERROR CONVERTING PAYMENT FORMAT TO BIGDECIMAL");
        }
    }
}

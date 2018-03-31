package be.llodavid.api.orderApi;

import be.llodavid.api.TestApplication;
import be.llodavid.api.itemApi.ItemMapper;
import be.llodavid.domain.Repository;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.filter.OrderedRequestContextFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Inject
    @Qualifier("ItemRepo")
    private Repository<Item> itemRepository;
    @Inject
    private OrderMapper orderMapper;

    @Inject
    private Repository<Order> orderRepository;
    @Inject
    private ItemMapper itemMapper;

    private Item item, item2, item3, item4;
    private Order order1, order2, order3;
    private OrderDTO orderDTO;

    @Before
    public void setUp() throws Exception {
        item = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 1 paw")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        item2 = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 2 paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(100))
                .withStock(14)
                .build();
        item3 = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 3 paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(130))
                .withStock(14)
                .build();
        item4 = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 4 paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(140))
                .withStock(14)
                .build();

        order1 = new Order(1,
                Arrays.asList(new ItemGroup(item,2),
                new ItemGroup(item2,3)));
        order1.finishOrder(LocalDate.now());
        order2 = new Order(1,
                Arrays.asList(new ItemGroup(item4,1)));
        order2.finishOrder(LocalDate.now());

        order3 = new Order(2,
                Arrays.asList(new ItemGroup(item3,2),
                        new ItemGroup(item2,1)));
        order3.finishOrder(LocalDate.now());

        orderRepository.addRecord(order1);
        orderRepository.addRecord(order2);
        orderRepository.addRecord(order3);

    }

    @Test
    public void getOrders_happyPath() {
        ResponseEntity<OrderDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s", port, "orders"), OrderDTO[].class);

        List<OrderDTO> orderDTOS = Arrays.asList(response.getBody());
        assertThat(orderDTOS).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderDTOS).contains(
                orderMapper.orderToDTO(order1),
                orderMapper.orderToDTO(order2),
                orderMapper.orderToDTO(order3));
    }

    @Test
    public void getOrder_happyPath() {
        ResponseEntity<OrderDTO> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "orders",order1.getId()), OrderDTO.class);

        orderDTO = response.getBody();
        assertThat(orderDTO).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderDTO).isEqualTo(orderMapper.orderToDTO(order1));
    }

    @Test
    public void getAllOrdersForCustomer_happyPath() {
        ResponseEntity<OrderDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "orders/customer",1), OrderDTO[].class);

        List<OrderDTO> orderDTOS = Arrays.asList(response.getBody());
        assertThat(orderDTOS).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderDTOS).contains(
                orderMapper.orderToDTO(order1),
                orderMapper.orderToDTO(order2));
        assertThat(orderDTOS).doesNotContain(
                orderMapper.orderToDTO(order3));
    }
}
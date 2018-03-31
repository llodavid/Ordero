package be.llodavid.api.shoppingApi;

import be.llodavid.api.TestApplication;
import be.llodavid.api.itemApi.ItemMapper;
import be.llodavid.api.orderApi.OrderDTO;
import be.llodavid.api.orderApi.OrderMapper;
import be.llodavid.domain.Repository;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.domain.order.Order;
import be.llodavid.service.ShoppingService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingControllerIntegrationTest {

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
    private ItemGroupMapper itemGroupMapper;

    @Inject
    private ShoppingService shoppingService;

    private static Item item, item2, item3, item4;
    private Order order1, order2, order3;
    private OrderDTO orderDTO;

    private ItemGroupDTO itemGroupDTO1, itemGroupDTO2;
    private ItemGroup itemGroup1, itemGroup2;
    private static boolean recordsAdded = false;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

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
        itemRepository.clear();

        itemRepository.addRecord(item);
        itemRepository.addRecord(item2);
        itemRepository.addRecord(item3);
        itemRepository.addRecord(item4);

        order1 = new Order(1,
                Arrays.asList(new ItemGroup(item, 2),
                        new ItemGroup(item2, 3)));
        order1.finishOrder(LocalDate.now());
        order2 = new Order(1,
                Arrays.asList(new ItemGroup(item4, 1)));
        order2.finishOrder(LocalDate.now());

        order3 = new Order(2,
                Arrays.asList(new ItemGroup(item3, 2),
                        new ItemGroup(item2, 1)));
        order3.finishOrder(LocalDate.now());

        itemGroup1 = new ItemGroup(item, 2);
        itemGroup2 = new ItemGroup(item2, 1);

    }

    @After
    public void tearDown() throws Exception {
        shoppingService.clearShoppingCart(1);
        shoppingService.clearShoppingCart(2);
    }

    @Test
    public void getShoppingCartContent_happyPath() {
        shoppingService.addItemToShoppingCart(itemGroup1, 1);
        shoppingService.addItemToShoppingCart(itemGroup2, 2);
        itemGroupDTO1 = itemGroupMapper.ItemGroupToDTO(itemGroup1);
        itemGroupDTO2 = itemGroupMapper.ItemGroupToDTO(itemGroup2);

        ResponseEntity<ItemGroupDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "shoppingcart", 1), ItemGroupDTO[].class);

        List<ItemGroupDTO> ItemGroupDTOS = Arrays.asList(response.getBody());
        assertThat(ItemGroupDTOS).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ItemGroupDTOS).contains(itemGroupDTO1);
        assertThat(ItemGroupDTOS.size()).isEqualTo(1);
        assertThat(ItemGroupDTOS).doesNotContain(itemGroupDTO2);
    }

    @Test
    public void createOrder_happyPath() {
        itemGroup1 = new ItemGroup(item, 2);
        itemGroup2 = new ItemGroup(item2, 1);

        shoppingService.addItemToShoppingCart(itemGroup1, 1);
        shoppingService.addItemToShoppingCart(itemGroup2, 2);
        itemGroup1.calculateShippingDate(LocalDate.now());
        itemGroup2.calculateShippingDate(LocalDate.now());
        itemGroupDTO1 = itemGroupMapper.ItemGroupToDTO(itemGroup1);
        itemGroupDTO2 = itemGroupMapper.ItemGroupToDTO(itemGroup2);

        //Todo: I have to add a valid orderDTO here even though i don't need it... Discuss with Niels how I can fix this.
        ResponseEntity<OrderDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s/%s", port, "shoppingcart", 1), orderMapper.orderToDTO(order1), OrderDTO.class);

        OrderDTO orderDTO = response.getBody();
        assertThat(orderDTO).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(orderDTO.orderItems).contains(itemGroupDTO1);
        assertThat(orderDTO.customerId).isEqualTo(1);
        assertThat(orderDTO.orderItems).doesNotContain(itemGroupDTO2);
    }

    @Test
    public void reOrder_happyPath() {
        orderRepository.addRecord(order1);

        //TODO i shouldn't have to use an ORDERDTO here, but with null, it won't work
        ResponseEntity<OrderDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s/%s/%s/%s", port, "customer", 1, "reorder", order1.getId()),
                        orderMapper.orderToDTO(order1), OrderDTO.class);

        orderDTO = response.getBody();
        assertThat(orderDTO.customerId).isEqualTo(order1.getCustomerId());
        assertThat(orderDTO.orderItems.get(0).itemId).isEqualTo(order1.getOrderItems().get(0).getItemId());
        assertThat(orderDTO.orderItems.get(0).amount).isEqualTo(order1.getOrderItems().get(0).getAmount());
        assertThat(orderDTO.orderItems.size()).isEqualTo(order1.getOrderItems().size());
    }

    @Test
    public void addItemToShoppingCart() {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.itemId = 1;
        cartItemDTO.amount = 3;
        ResponseEntity<ItemGroupDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s/%s", port, "product", 1), cartItemDTO, ItemGroupDTO.class);

        assertThat(response.getBody().amount).isEqualTo(3);
        assertThat(response.getBody().itemId).isEqualTo(1);
    }
}
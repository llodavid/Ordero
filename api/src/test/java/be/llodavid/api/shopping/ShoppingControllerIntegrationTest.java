package be.llodavid.api.shopping;

import be.llodavid.api.TestApplication;
import be.llodavid.api.orders.OrderDTO;
import be.llodavid.api.orders.OrderMapper;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerRepository;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.items.ItemRepository;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.Order;
import be.llodavid.domain.orders.OrderRepository;
import be.llodavid.service.ShoppingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Transactional
public class ShoppingControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Inject
    private ItemRepository itemRepository;
    @Inject
    private OrderMapper orderMapper;

    @Inject
    private OrderRepository orderRepository;
    @Inject
    private ItemGroupMapper itemGroupMapper;

    @Inject
    private ShoppingService shoppingService;

    private static Item item, item2, item3, item4;
    private Order order1, order2, order3;
    private OrderDTO orderDTO;

    private ItemGroupDTO itemGroupDTO1, itemGroupDTO2;
    private ItemGroup itemGroup1, itemGroup2;
    private Customer customer1, customer2;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() throws Exception {
        clearAndFlushTables();

        createItems();
        createCustomers();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        createOrders();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        itemGroup1 = new ItemGroup(item, 2);
        itemGroup2 = new ItemGroup(item2, 1);
    }

    private void createOrders() {
        order1 = new Order(customer1.getId(),
                Arrays.asList(new ItemGroup(item, 2),
                        new ItemGroup(item2, 3)));
        order1.finishOrder(LocalDate.now());
        order2 = new Order(customer1.getId(),
                Arrays.asList(new ItemGroup(item4, 1)));
        order2.finishOrder(LocalDate.now());

        order3 = new Order(customer2.getId(),
                Arrays.asList(new ItemGroup(item3, 2),
                        new ItemGroup(item2, 1)));
        order3.finishOrder(LocalDate.now());

        orderRepository.saveAll(Arrays.asList(order1,order2,order3));
    }

    private void createItems() {
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
        itemRepository.saveAll(Arrays.asList(item,item2,item3,item4));
    }

    private void createCustomers() {
        customer1 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("David")
                .withLastName("Van den Bergh")
                .withEmail("david@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("steenweg")
                        .withHousenumber("65s")
                        .withCity("FLANDERS")
                        .withZipcode("1193")
                        .build())
                .build();
        customer2 = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Bruce")
                .withLastName("Whiner")
                .withEmail("BATMAN@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("BatCave")
                        .withHousenumber("99")
                        .withCity("Gent")
                        .withZipcode("9000")
                        .build())
                .build();
        customerRepository.saveAll((Arrays.asList(customer1,customer2)));
    }


    @AfterEach
    public void tearDown() throws Exception {
        shoppingService.clearShoppingCart(1);
        shoppingService.clearShoppingCart(2);
    }

    public void clearAndFlushTables() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        customerRepository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
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

        shoppingService.addItemToShoppingCart(itemGroup1, customer1.getId());
        shoppingService.addItemToShoppingCart(itemGroup2, customer2.getId());
        itemGroup1.calculateShippingDate(LocalDate.now());
        itemGroup2.calculateShippingDate(LocalDate.now());
        itemGroupDTO1 = itemGroupMapper.ItemGroupToDTO(itemGroup1);
        itemGroupDTO2 = itemGroupMapper.ItemGroupToDTO(itemGroup2);

        //Todo: I have to add a valid orderDTO here even though i don't need it... Discuss with Niels how I can fix this.
        ResponseEntity<OrderDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s/%s", port, "shoppingcart", customer1.getId()), orderMapper.orderToDTO(order1), OrderDTO.class);

        OrderDTO orderDTO = response.getBody();
        assertThat(orderDTO).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(orderDTO.orderItems).contains(itemGroupDTO1);
        assertThat(orderDTO.customerId).isEqualTo(customer1.getId());
        assertThat(orderDTO.orderItems).doesNotContain(itemGroupDTO2);
    }

    @Test
    public void reOrder_happyPath() {
        //TODO i shouldn't have to use an ORDERDTO here, but with null, it won't work
        ResponseEntity<OrderDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s/%s/%s/%s", port, "customers", customer1.getId(), "reorder", order1.getId()),
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
        cartItemDTO.itemId = item.getId();
        cartItemDTO.amount = 3;
        ResponseEntity<ItemGroupDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s/%s", port, "product", item.getId()), cartItemDTO, ItemGroupDTO.class);

        assertThat(response.getBody().amount).isEqualTo(3);
        assertThat(response.getBody().itemId).isEqualTo(item.getId());
    }
}
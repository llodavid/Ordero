package be.llodavid.api.orders;

import be.llodavid.api.TestApplication;
import be.llodavid.api.items.ItemMapper;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerRepository;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.items.ItemRepository;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.domain.orders.Order;
import be.llodavid.domain.orders.OrderRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(JUnitPlatform.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Transactional
public class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemMapper itemMapper;

    private Item item, item2, item3, item4;
    private Order order1, order2, order3;
    private OrderDTO orderDTO;
    private Customer customer1, customer2;

    @BeforeEach
    public void setUp() throws Exception {
        clearAndFlushDatabases();
        createItems();
        createCustomers();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        createOrders();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    private void createOrders() {
        order1 = new Order(customer1.getId(),
                Arrays.asList(new ItemGroup(item,2),
                new ItemGroup(item2,3)));
        order1.finishOrder(LocalDate.now());
        order2 = new Order(customer1.getId(),
                Arrays.asList(new ItemGroup(item4,1)));
        order2.finishOrder(LocalDate.now());

        order3 = new Order(customer2.getId(),
                Arrays.asList(new ItemGroup(item3,2),
                        new ItemGroup(item2,1)));
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

    public void clearAndFlushDatabases() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        customerRepository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
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
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "orders/customers",customer1.getId()), OrderDTO[].class);

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
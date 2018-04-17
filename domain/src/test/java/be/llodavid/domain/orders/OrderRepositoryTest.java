package be.llodavid.domain.orders;

import be.llodavid.configuration.databaseconfig.DatabaseConfig;
import be.llodavid.domain.customers.Address;
import be.llodavid.domain.customers.Customer;
import be.llodavid.domain.customers.CustomerRepository;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.items.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.transaction.TestTransaction;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringJUnitConfig(DatabaseConfig.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = NONE)
public class OrderRepositoryTest {

    private OrderRepository orderDBRepository;
    private ItemRepository itemRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public OrderRepositoryTest(OrderRepository orderRepository, ItemRepository itemRepository, CustomerRepository customerRepository) {
        this.orderDBRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
    }


    @Test
    @Transactional
    public void createOrder_happyPath() {
        ItemGroup itemGroup1, itemGroup2;
        Customer customer = Customer.CustomerBuilder.buildCustomer()
                .withFirstName("Piet")
                .withLastName("HuysenTruyt")
                .withEmail("KortePiet2@hotmail.com")
                .withPhonenumber("02/224 45 35")
                .withAddress(Address.AddressBuilder.buildAddress()
                        .withStreet("RockyRoqd")
                        .withHousenumber("69")
                        .withCity("FLANDERS")
                        .withZipcode("9473")
                        .build())
                .build();

        customerRepository.save(customer);

        assertThat(customer.getId()).isNotEqualTo(0);
        Item item1, item2;
        item1 = Item.ItemBuilder.buildItem()
                .withName("LG 55 inch OLED TV")
                .withDescription("TV")
                .withPrice(new BigDecimal(1500))
                .withStock(6)
                .build();
        item2 = Item.ItemBuilder.buildItem()
                .withName("Chair with two paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemGroup1 = new ItemGroup(item1, 2);
        itemGroup2 = new ItemGroup(item2, 1);
        Order order1 = new Order(customer.getId(),
                Arrays.asList(itemGroup1, itemGroup2));
        order1.finishOrder(LocalDate.now().minusDays(1));

//        TestTransaction.flagForCommit();
//        TestTransaction.end();
//        TestTransaction.start();

        orderDBRepository.save(order1);
        //order1.addItemGroup(new ItemGroup(item1, 2) );
        assertThat(order1.getId()).isNotEqualTo(0);
        assertThat(itemGroup1.getItemGroupId()).isNotEqualTo(0);
    }
}
package be.llodavid.api.items;

import be.llodavid.api.TestApplication;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.items.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Transactional
public class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemMapper itemMapper;

    private Item item, item2, item3, item4;
    private ItemDTO itemDTO;


    @BeforeEach
    public void setUp() throws Exception {
        clearAndFlushTables();
        item = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 1 paw")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        item2 = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 2 paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        item3 = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 3 paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();
        item4 = Item.ItemBuilder.buildItem()
                .withName("Simple Chair with 4 paws")
                .withDescription("extra paws cost extra")
                .withPrice(new BigDecimal(70))
                .withStock(14)
                .build();

        itemDTO = itemMapper.itemToDTO(item);
    }

    public void clearAndFlushTables() {
        itemRepository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @Test
    public void createItem_happyPath() {
        //itemRepository.addRecord(items);
        ItemDTO createdItem = new TestRestTemplate()
                .postForObject(String.format("http://localhost:%s/%s", port, "items"), itemDTO, ItemDTO.class);

        assertThat(createdItem).isNotNull();
        assertThat(createdItem.name).isEqualTo("Simple Chair with 1 paw");
        assertThat(createdItem.description).isEqualTo("extra paws cost extra");
    }

    @Test
    public void getItem_happyPath() {
        itemRepository.save(item2);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        ResponseEntity<ItemDTO> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s/%s", port, "items",item2.getId()), ItemDTO.class);

        ItemDTO itemDTO2 = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(itemDTO2).isNotNull();
        assertThat(itemDTO2.itemId).isEqualTo(item2.getId());
        assertThat(itemDTO2.name).isEqualTo("Simple Chair with 2 paws");
        assertThat(itemDTO2.description).isEqualTo("extra paws cost extra");
    }

    @Test
    public void getItems_happyPath() {
        itemRepository.save(item2);
        itemRepository.save(item3);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        ResponseEntity<ItemDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s", port, "items"), ItemDTO[].class);

        List<ItemDTO> itemList = Arrays.asList(response.getBody());
        assertThat(itemList).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //ItemDTO itemDTO2 = itemList.get(itemList.size()-1);
        assertThat(itemList).hasSize(2);
        assertThat(itemList).contains(itemMapper.itemToDTO(item2));
        assertThat(itemList).contains(itemMapper.itemToDTO(item3));
    }

    @Test
    public void updateItem_happyPath() {
        itemRepository.save(item4);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        itemDTO=itemMapper.itemToDTO(item4);
        ResponseEntity<ItemDTO> response = new TestRestTemplate()
                .exchange(format("http://localhost:%s/%s/%s", port, "items", item4.getId()),
                        HttpMethod.PUT,
                        new HttpEntity<>(itemDTO),
                        ItemDTO.class);
                //.postForEntity(String.format("http://localhost:%s/%s/%s", port, "items",item4.getId()), itemDTO, ItemDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.getBody().name).isEqualTo("Simple Chair with 4 paws");
        assertThat(response.getBody().description).isEqualTo("extra paws cost extra");
        assertThat(response.getBody().price).isEqualTo(new BigDecimal(70));
    }
}
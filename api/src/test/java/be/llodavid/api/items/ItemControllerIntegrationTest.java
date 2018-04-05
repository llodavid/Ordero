package be.llodavid.api.items;

import be.llodavid.api.TestApplication;
import be.llodavid.domain.Repository;
import be.llodavid.domain.items.Item;
import org.junit.Before;
import org.junit.Test;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Inject
    @Qualifier("ItemRepo")
    private Repository<Item> itemRepository;
    @Inject
    private ItemMapper itemMapper;

    private Item item, item2, item3, item4;
    private ItemDTO itemDTO;


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
        itemRepository.addRecord(item2);

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
        itemRepository.addRecord(item3);
        ResponseEntity<ItemDTO[]> response = new TestRestTemplate()
                .getForEntity(String.format("http://localhost:%s/%s", port, "items"), ItemDTO[].class);

        List<ItemDTO> itemList = Arrays.asList(response.getBody());
        assertThat(itemList).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ItemDTO itemDTO2 = itemList.get(itemList.size()-1);
        assertThat(itemDTO2.itemId).isEqualTo(itemList.size());
        assertThat(itemDTO2.name).isEqualTo("Simple Chair with 3 paws");
        assertThat(itemDTO2.description).isEqualTo("extra paws cost extra");
    }

    @Test
    public void updateItem_happyPath() {
        //itemRepository.addRecord(items);
        itemDTO=itemMapper.itemToDTO(item4);
        ResponseEntity<ItemDTO> response = new TestRestTemplate()
                .postForEntity(String.format("http://localhost:%s/%s", port, "items"), itemDTO, ItemDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.getBody().name).isEqualTo("Simple Chair with 4 paws");
        assertThat(response.getBody().description).isEqualTo("extra paws cost extra");
        assertThat(response.getBody().price).isEqualTo(new BigDecimal(70));
    }
}
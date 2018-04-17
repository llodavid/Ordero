package be.llodavid.domain.items;

import be.llodavid.configuration.databaseconfig.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringJUnitConfig(DatabaseConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ItemRepositoryTest {

    private ItemRepository itemDBRepository;

    @Autowired
    public ItemRepositoryTest(ItemRepository itemDBRepository) {
        this.itemDBRepository = itemDBRepository;
    }

    @Test
    public void createItem_happyPath(){
        Item item = itemDBRepository.save(Item.ItemBuilder.buildItem()
                .withName("LG 55 inch OLED TV")
                .withDescription("TV")
                .withPrice(new BigDecimal(1500))
                .withStock(6)
                .build());


        assertThat(item.getId()).isNotEqualTo(0);
    }
}
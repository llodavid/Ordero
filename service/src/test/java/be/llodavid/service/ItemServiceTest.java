package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.item.ItemData;
import be.llodavid.service.exceptions.DoubleEntryException;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class ItemServiceTest {
    Item item1;
    Item item2;
    private Repository<Item> itemRepository;
    private ItemService itemService;
    private ItemData itemData;

    @Before
    public void setUp() throws Exception {
        itemRepository = mock(Repository.class);
        itemData = mock(ItemData.class);
        itemService = new ItemService(itemRepository);

        //TODO: write mocks for items
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
    }

    @Test
    public void injectDefaultData_happyPath() {
        when(itemData.getDefaultItems()).thenReturn(new ArrayList<>());
        itemService.injectDefaultData();
        verify(itemRepository, times(1)).injectDefaultData(new ItemData().getDefaultItems());
    }

    @Test
    public void getItem_happyPath() {
        when(itemRepository.getRecordById(1)).thenReturn(item1);
        when(itemRepository.recordExists(1)).thenReturn(true);
        Assertions.assertThat(itemService.getItem(1)).isEqualTo(item1);
    }

    @Test
    public void getItem_givenItemThatDoesNotExist_throwsException() {
        when(itemRepository.getRecordById(1)).thenReturn(item1);
        when(itemRepository.recordExists(1)).thenReturn(true);
        Assertions.assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> itemService.getItem(15)).withMessage("The item could not be found based on the provided item ID: 15.");
    }

    @Test
    public void addItem_happyPath() {
        when(itemRepository.addRecord(item1)).thenReturn(item1);
        Assertions.assertThat(itemService.addItem(item1)).isEqualTo(item1);
    }

    @Test
    public void addItem_givenItemThatAlreadyExists_throwsException() {
        when(itemRepository.recordAlreadyInRepository(item1)).thenReturn(true);
        Assertions.assertThatExceptionOfType(DoubleEntryException.class).isThrownBy(() -> itemService.addItem(item1)).withMessage("The item LG 55 inch OLED TV is already present in the system.");
    }

    @Test
    public void getAllItems_happyPath() {
        when(itemRepository.getAllRecords()).thenReturn(Arrays.asList(item1, item2));
        Assertions.assertThat(itemService.getAllItems()).isEqualTo(Arrays.asList(item1, item2));
    }
}
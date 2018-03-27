package be.llodavid.service;

import be.llodavid.domain.Repository;
import be.llodavid.domain.item.Item;
import be.llodavid.domain.item.ItemData;
import be.llodavid.domain.order.ItemGroup;
import be.llodavid.service.exceptions.DoubleEntryException;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class ItemServiceUnitTest {
    private Item item, item1, item2;
    private Repository<Item> itemRepository;
    private ItemService itemService;
    private ItemData itemData;

    @Before
    public void setUp() throws Exception {
        itemRepository = mock(Repository.class);
        itemData = mock(ItemData.class);
        itemService = new ItemService(itemRepository);

        item = mock(Item.class);

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
        assertThat(itemService.getItem(1)).isEqualTo(item1);
    }

    @Test
    public void getItem_givenItemThatDoesNotExist_throwsException() {
        when(itemRepository.getRecordById(1)).thenReturn(item1);
        when(itemRepository.recordExists(1)).thenReturn(true);
        assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> itemService.getItem(15)).withMessage("The item could not be found based on the provided item ID: 15.");
    }

    @Test
    public void addItem_happyPath() {
        when(itemRepository.addRecord(item1)).thenReturn(item1);
        assertThat(itemService.addItem(item1)).isEqualTo(item1);
    }

    @Test
    public void addItem_givenItemThatAlreadyExists_throwsException() {
        when(itemRepository.recordAlreadyInRepository(item1)).thenReturn(true);
        assertThatExceptionOfType(DoubleEntryException.class).isThrownBy(() -> itemService.addItem(item1)).withMessage("The item LG 55 inch OLED TV is already present in the system.");
    }

    @Test
    public void getAllItems_happyPath() {
        when(itemRepository.getAllRecords()).thenReturn(Arrays.asList(item1, item2));
        assertThat(itemService.getAllItems()).isEqualTo(Arrays.asList(item1, item2));
    }

    @Test
    public void modifyStock_happyPath() {
        ItemGroup itemGroup = mock(ItemGroup.class);
        when(itemRepository.getRecordById(1)).thenReturn(item);
        when(itemGroup.getAmount()).thenReturn(2);
        when(itemGroup.getItemId()).thenReturn(1);
        when(item.getId()).thenReturn(1);

        itemService.modifyStock(Arrays.asList(itemGroup));
        verify(item, times(1)).decreaseStock(2);
        verify(itemRepository, times(1)).updateRecord(item, 1);
    }

    @Test
    public void updateItem_happyPath() {
        when(itemRepository.recordExists(1)).thenReturn(true);
        when(itemRepository.updateRecord(item,1)).thenReturn(item);
        assertThat(itemService.updateItem(item,1)).isEqualTo(item);
    }
    @Test
    public void updateItem_givenNonExistingCustomer_throwsException() {
        when(itemRepository.recordExists(10)).thenReturn(false);
        when(itemRepository.updateRecord(item,1)).thenReturn(item);
        assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> itemService.updateItem(item, 10)).withMessage("The item could not be found based on the provided item ID: 10.");
    }

    @Test
    public void createItemGroup_happyPath() {
        when(itemRepository.recordExists(1)).thenReturn(true);
        item1.setId(1);
        when(itemRepository.getRecordById(1)).thenReturn(item1);
        Assertions.assertThat(itemService.createItemGroup(1,1)).isEqualTo(new ItemGroup(item1,1));
    }
}
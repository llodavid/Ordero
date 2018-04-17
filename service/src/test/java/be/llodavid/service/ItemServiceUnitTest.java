package be.llodavid.service;

import be.llodavid.domain.OrderoRepository;
import be.llodavid.domain.items.Item;
import be.llodavid.domain.items.ItemData;
import be.llodavid.domain.items.ItemRepository;
import be.llodavid.domain.orders.ItemGroup;
import be.llodavid.util.exceptions.DoubleEntryException;
import be.llodavid.util.exceptions.UnknownResourceException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class ItemServiceUnitTest {
    private Item item, item1, item2;
    private ItemRepository itemRepository;
    private ItemService itemService;
    private ItemData itemData;

    @Before
    public void setUp() throws Exception {
        itemRepository = mock(ItemRepository.class);
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
    public void getItem_happyPath() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.existsById(1L)).thenReturn(true);
        assertThat(itemService.getItem(1)).isEqualTo(item1);
    }

    @Test
    public void getItem_givenItemThatDoesNotExist_throwsException() {
//        when(itemRepository.findById(1)).thenReturn(item1);
        when(itemRepository.existsById(1L)).thenReturn(true);
        assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> itemService.getItem(15)).withMessage("The items could not be found based on the provided items ID: 15.");
    }

    @Test
    public void addItem_happyPath() {
        when(itemRepository.save(item1)).thenReturn(item1);
        assertThat(itemService.addItem(item1)).isEqualTo(item1);
    }

    @Test
    public void addItem_givenItemThatAlreadyExists_throwsException() {
        when(itemRepository.existsById(item1.getId())).thenReturn(true);
        assertThatExceptionOfType(DoubleEntryException.class).isThrownBy(() -> itemService.addItem(item1)).withMessage("The items LG 55 inch OLED TV is already present in the system.");
    }

    @Test
    public void getAllItems_happyPath() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));
        assertThat(itemService.getAllItems()).isEqualTo(Arrays.asList(item1, item2));
    }

    @Test
    public void modifyStock_happyPath() {
        ItemGroup itemGroup = mock(ItemGroup.class);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemGroup.getAmount()).thenReturn(2);
        when(itemGroup.getItemId()).thenReturn(1L);
        when(item.getId()).thenReturn(1L);
        when(itemRepository.existsById(1L)).thenReturn(true);

        itemService.modifyStock(Arrays.asList(itemGroup));
        verify(item, times(1)).decreaseStock(2);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void updateItem_happyPath() {
        when(item.getId()).thenReturn(1L);
        when(itemRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.save(item)).thenReturn(item);
        assertThat(itemService.updateItem(item)).isEqualTo(item);
    }
    @Test
    public void updateItem_givenNonExistingCustomer_throwsException() {
        when(item.getId()).thenReturn(10L);
        when(itemRepository.existsById(10L)).thenReturn(false);
        when(itemRepository.save(item)).thenReturn(item);
        assertThatExceptionOfType(UnknownResourceException.class).isThrownBy(() -> itemService.updateItem(item)).withMessage("The items could not be found based on the provided items ID: 10.");
    }

    @Test
    public void createItemGroup_happyPath() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        item1.setId(1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        Assertions.assertThat(itemService.createItemGroup(1,1)).isEqualTo(new ItemGroup(item1,1));
    }
}
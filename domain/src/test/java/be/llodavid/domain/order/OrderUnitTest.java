package be.llodavid.domain.order;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static be.llodavid.domain.order.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class OrderTest {

    ItemGroup itemGroup1, itemGroup2;
    Order order;

    @Before
    public void setUp() throws Exception {
        itemGroup1 = mock(ItemGroup.class);
        itemGroup2 = mock(ItemGroup.class);

        when(itemGroup1.calculateItemGroupTotal()).thenReturn(new BigDecimal(100));
        when(itemGroup2.calculateItemGroupTotal()).thenReturn(new BigDecimal(200));
        //Mockito.doReturn(true).when(itemGroup1.calculateShippingDate(LocalDate.now()));
    }

    @Test
    public void finishOrder_happyPath() {
        order = new Order(1, Arrays.asList(itemGroup1,itemGroup2));
        order.finishOrder(LocalDate.of(2018,03,26));

        assertThat(order.getTotalAmount()).isEqualTo(new BigDecimal(300));
        assertThat(order.getOrderStatus()).isEqualTo(PAID);

        verify(itemGroup1, times(1)).calculateShippingDate(LocalDate.of(2018,03,26));
        verify(itemGroup2, times(1)).calculateShippingDate(LocalDate.of(2018,03,26));
    }

    @Test
    public void finishOrder_givenAlreadyPaidOrder_throwsException() {
        order = new Order(1, Arrays.asList(itemGroup1,itemGroup2));
        order.finishOrder(LocalDate.of(2018,03,26));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->order.finishOrder(LocalDate.of(2018,03,26)));
    }
}
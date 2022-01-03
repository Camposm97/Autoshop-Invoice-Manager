package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ItemTest {
    @Test
    public void test1() {
        Item item = new Item("IEEE", "Single Precision",
                9.99, 4.99, 2, true);
        assertEquals("IEEE", item.getId());
        assertEquals("Single Precision", item.getDesc());
        assertEquals(9.99, item.getRetailPrice());
        assertEquals(4.99, item.getListPrice());
        assertEquals(true, item.isTaxable());
        assertEquals(2, item.getQuantity());
        assertEquals("21.70", String.format("%.2f", item.bill()));
    }
}

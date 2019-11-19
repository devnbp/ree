package com.company.pr2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void constructor() {
        Item item = new Item("Apple", 0.99, 5, Item.ItemType.NEW);

        assertEquals("Apple", item.getFieldValue("title"));
        assertEquals(0.99, item.getFieldValue("price"));
        assertEquals(5, item.getFieldValue("quantity"));
        assertEquals(Item.ItemType.NEW, item.getFieldValue("type"));

        assertThrows(IllegalArgumentException.class, () -> new Item("", 0.99, 5, Item.ItemType.NEW));
        assertThrows(IllegalArgumentException.class, () -> new Item("Apple", -5, 5, Item.ItemType.NEW));
        assertThrows(IllegalArgumentException.class, () -> new Item("Apple", 0.99, -5, Item.ItemType.NEW));
    }

    @ParameterizedTest
    @MethodSource("calculateDiscountParams")
    void calculateDiscount(Item.ItemType type, int quantity, int discount) {
        Item item = new Item("Apple", 0.99, quantity, type);

        assertEquals(discount, item.calculateDiscount());
    }

    private static Stream<Arguments> calculateDiscountParams() {
        return Stream.of(
                Arguments.of(Item.ItemType.NEW, 1, 0),
                Arguments.of(Item.ItemType.NEW, 100, 0),
                Arguments.of(Item.ItemType.REGULAR, 1, 0),
                Arguments.of(Item.ItemType.REGULAR, 100, 10),
                Arguments.of(Item.ItemType.SECOND_FREE, 1, 0),
                Arguments.of(Item.ItemType.SECOND_FREE, 2, 50),
                Arguments.of(Item.ItemType.SECOND_FREE, 100, 60),
                Arguments.of(Item.ItemType.SALE, 1, 70),
                Arguments.of(Item.ItemType.SALE, 100, 80),
                Arguments.of(Item.ItemType.SALE, 200, 80)
        );
    }

    @Test
    void calculateTotal() {
        Item item = new Item("Apple", 0.99, 5, Item.ItemType.NEW);

        assertEquals(4.95, item.getFieldValue("total"));
    }
}
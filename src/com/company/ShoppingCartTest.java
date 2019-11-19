package com.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private static int[] types = new int[0];

    public int[] itemTypes() throws IllegalAccessException {
        if (types.length == 0) {
            ShoppingCart obj = new ShoppingCart();
            String contains = "ITEM_";

            Map<Integer, Field> map = new HashMap<>(){{
                int i = 0;
                for (Field field:ShoppingCart.class.getFields()) {
                    put(i, field);
                    i++;
                }
            }};

            Map<Integer, Field> itemTypes = map.entrySet().stream()
                    .filter(m -> m.getValue().getName().contains(contains))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            int[] arr = new int[itemTypes.size()];

            int i = 0;
            for (Map.Entry<Integer, Field> entry : itemTypes.entrySet()) {
                Field field = entry.getValue();
                arr[i] = (int) field.get(obj);
                i++;
            }

            types = arr;
        }

        return types;
    }

    @Test
    void testAddItemTitle() {
        ShoppingCart cart = new ShoppingCart();

        cart.addItem("Apple", 0.99, 5, ShoppingCart.ITEM_REGULAR);

        ShoppingCart.Item item = (ShoppingCart.Item) cart.items.get(cart.items.size()-1);

        assertTrue(item.title.length() > 0 && item.title.length() <= 32);

        assertThrows(IllegalArgumentException.class, () -> cart.addItem("", 0.99, 5, ShoppingCart.ITEM_REGULAR));
    }

    @Test
    void testAddItemPrice() {
        ShoppingCart cart = new ShoppingCart();

        cart.addItem("Apple", 0.99, 5, ShoppingCart.ITEM_REGULAR);

        ShoppingCart.Item item = (ShoppingCart.Item) cart.items.get(cart.items.size()-1);

        assertTrue(item.price > 0 && item.price < 1000);

        assertThrows(IllegalArgumentException.class, () -> cart.addItem("Apple", -0.99, 5, ShoppingCart.ITEM_REGULAR));
    }

    @Test
    void testAddItemQuantity() {
        ShoppingCart cart = new ShoppingCart();

        cart.addItem("Apple", 0.99, 5, ShoppingCart.ITEM_REGULAR);

        ShoppingCart.Item item = (ShoppingCart.Item) cart.items.get(cart.items.size()-1);

        assertTrue(item.quantity > 0 && item.quantity <= 1000);

        assertThrows(IllegalArgumentException.class, () -> cart.addItem("Apple", 0.99, 0, ShoppingCart.ITEM_REGULAR));
    }

    @Test
    void testAddItemType() throws IllegalAccessException {
        ShoppingCart cart = new ShoppingCart();

        int[] types = itemTypes();

        cart.addItem("Apple", 0.99, 5, ShoppingCart.ITEM_REGULAR);

        ShoppingCart.Item item = (ShoppingCart.Item) cart.items.get(cart.items.size()-1);

        assertTrue(IntStream.of(types).anyMatch(type -> type == item.type));
    }

    @Test
    void testAddItemCount() {
        ShoppingCart cart = new ShoppingCart();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            for (int i = 0; i < 100; i++) {
                cart.addItem("Apple", 0.99, 5, ShoppingCart.ITEM_REGULAR);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("calculateDiscountParams")
    void testCalculateDiscount(int type, int quantity, int discount) {
        ShoppingCart cart = new ShoppingCart();

        cart.addItem("Apple", 0.99, quantity, type);

        ShoppingCart.Item item = (ShoppingCart.Item) cart.items.get(cart.items.size()-1);

        assertEquals(discount, ShoppingCart.calculateDiscount(item));
    }

    private static Stream<Arguments> calculateDiscountParams() {
        return Stream.of(
                Arguments.of(ShoppingCart.ITEM_REGULAR, 1, 0),
                Arguments.of(ShoppingCart.ITEM_REGULAR, 100, 10),
                Arguments.of(ShoppingCart.ITEM_SECOND_FREE, 1, 0),
                Arguments.of(ShoppingCart.ITEM_SECOND_FREE, 2, 50),
                Arguments.of(ShoppingCart.ITEM_SECOND_FREE, 100, 60),
                Arguments.of(ShoppingCart.ITEM_DISCOUNT, 1, 10),
                Arguments.of(ShoppingCart.ITEM_DISCOUNT, 10, 20),
                Arguments.of(ShoppingCart.ITEM_DISCOUNT, 100, 60),
                Arguments.of(ShoppingCart.ITEM_FOR_SALE, 1, 80)
        );
    }
}
package com.company.pr2;

import java.util.*;
import com.company.pr2.Item.ItemType;
/**
 * Containing items and calculating price.
 */
public class ShoppingCart
{
    private List<Item> items = new ArrayList<Item>();

    /**
     * Adds new item.
     *
     * @param title item title 1 to 32 symbols
     * @param price item ptice in USD, > 0
     * @param quantity item quantity, from 1
     * @param type item type
     *
     * @throws IllegalArgumentException if some value is wrong
     */
    public void addItem(String title, double price, int quantity, ItemType type) {
        Item item = new Item(title, price, quantity, type);

        items.add(item);
    }

    /**
     * Create and render ticket for cart items
     * @return rendered ticket string
     */
    public String ticket() {
        Ticket t = new Ticket(items);

        return t.render();
    }
}

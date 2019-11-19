package com.company.pr2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void render() {
        List<Item> items = new ArrayList<Item>();

        Ticket t = new Ticket(items);

        String str = "No items.";

        assertEquals(str, t.render());

        items.add(new Item("Apple", 0.99, 5, Item.ItemType.NEW));
        items.add(new Item("Banana", 20.00, 4, Item.ItemType.SECOND_FREE));

        t = new Ticket(items);

        str = "# Item    Price Quan. Discount  Total \n" +
                "-------------------------------------\n" +
                "1 Apple    $.99     5        -  $4.95 \n" +
                "2 Banana $20.00     4      50% $40.00 \n" +
                "-------------------------------------\n" +
                "2                              $44.95 \n";

        assertEquals(str, t.render());
    }
}
package com.company.pr2;

import java.util.HashMap;

public class Item implements HasValidator {
    /**
     * Validation rules
     */
    public HashMap<String, HashMap<String, Object>> rules = new HashMap<>() {{
        put("title", new HashMap<>() {{
            put("minLength", 0);
            put("maxLength", 33);
        }});
        put("price", new HashMap<>() {{
            put("min", 0);
        }});
        put("quantity", new HashMap<>() {{
            put("min", 0);
        }});
        put("type", new HashMap<>() {{
            put("inEnum", ItemType.values());
        }});
    }};

    public static enum ItemType { NEW, REGULAR, SECOND_FREE, SALE }

    String title;
    double price;
    int quantity;
    ItemType type;
    int discount;
    double total;

    public Item(String _title, double _price, int _quantity, ItemType _type) {
        setFieldValue("title", _title);
        setFieldValue("price", _price);
        setFieldValue("quantity", _quantity);
        setFieldValue("type", _type);
        setFieldValue("discount", calculateDiscount());
        setFieldValue("total", calculateTotal());
    }

    /**
     * Calculates item's discount.
     * For NEW item discount is 0%;
     * For SECOND_FREE item discount is 50% if quantity > 1
     * For SALE item discount is 70%
     * For each full 10 not NEW items item gets additional 1% discount,
     * but not more than 80% total
     */
    public int calculateDiscount()
    {
        int discount = 0;
        switch (type) {
            case NEW:
                return 0;

            case REGULAR:
                discount = 0;
                break;

            case SECOND_FREE:
                if (quantity > 1)
                    discount = 50;
                break;
            case SALE:
                discount = 70;
                break;
        }

        discount += quantity / 10;
        if (discount > 80)
            discount = 80;

        return discount;
    }

    /**
     * @return calculated total item price
     */
    public double calculateTotal()
    {
        return (Double) getFieldValue("price") * (Integer) getFieldValue("quantity") * (100.00 - calculateDiscount()) / 100.00;
    }
}

package com.company.pr2;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private List<Item> itemList;
    private int count = 0;
    private List<Integer> separatorAt = new ArrayList<>();
    private String separator = "-";
    private String[] header =  {"#", "Item", "Price", "Quan.", "Discount", "Total"};
    private double total = 0;
    private int[] aligns = new int[] { 1, -1, 1, 1, 1, 1 };
    private int[] width = new int[] { 0, 0, 0, 0, 0, 0 };
    private List<String[]> lines = new ArrayList<String[]>();

    private static final NumberFormat MONEY;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        MONEY = new DecimalFormat("$#.00", symbols);
    }

    public Ticket(List<Item> _items) {
        itemList = _items;
    }

    /**
     * @return rendered ticket string
     */
    public String render() {
        return formatTicket(itemList);
    }

    /**
     * Calculating columns max width
     */
    private void calculateWidths() {
        for (int i = 0; i < width.length; i++) {
            width[i] = columnMaxLength(i);
        }
    }

    /**
     * @param index column index
     * @return biggest width on column of lines values
     */
    private int columnMaxLength(int index) {
        int max = 0;

        for (String[] line : lines) {
            if (index < line.length) {
                max = (int) Math.max(max, line[index].length());
            }
        }

        return max;
    }

    /**
     * @param s separator char
     * @return separator line for current table
     */
    private String separator(String s) {
        int lineLength = width.length - 1;

        for (int w : width) {
            lineLength += w;
        }

        return s.repeat(lineLength)+"\n";
    }

    /**
     * @return footer string
     */
    private String[] footer() {
        return new String[] { String.valueOf(count), "", "", "", "", MONEY.format(total) };
    }

    /**
     * @param item item object
     * @return table line
     */
    private String[] line(Item item) {
        count++;

        return new String[] {
                String.valueOf(count),
                (String) item.getFieldValue("title"),
                MONEY.format(item.getFieldValue("price")),
                String.valueOf(item.getFieldValue("quantity")),
                ((Integer) item.getFieldValue("discount") == 0) ? "-" : (String.valueOf((Integer) item.getFieldValue("discount")) + "%"),
                MONEY.format(item.getFieldValue("total"))
        };
    }

    /**
     * @param line table line
     * @return rendered table line
     */
    private String renderLine(String[] line) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length; i++) {
            appendFormatted(sb, line[i], aligns[i], width[i]);
        }

        sb.append("\n");

        return sb.toString();
    }

    /**
     * Formats shopping price.
     * @param items list of items
     * @return string as lines, separated with \n,
     * first line: # Item Price Quan. Discount Total
     * second line: ---------------------------------------------------------
     * next lines: NN Title $PP.PP Q DD% $TT.TT
     * 1 Some title $.30 2 - $.60
     * 2 Some very long $100.00 1 50% $50.00
     * ...
     * 31 Item 42 $999.00 1000 - $999000.00
     * end line: ---------------------------------------------------------
     * last line: 31 $999050.60
     *
     * if no items in cart returns "No items." string.
     */
    private String formatTicket(List<Item> items) {
        if (items.size() == 0)
            return "No items.";

        lines.add(header);
        separatorAt.add(lines.size());

        for (Item item : items) {
            lines.add(line(item));
            total += (Double) item.getFieldValue("total");
        }

        separatorAt.add(lines.size());
        lines.add(footer());

        // columns max length
        calculateWidths();

        // render table
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            boolean shouldSeparate = false;

            for (int at : separatorAt) {
                if (i == at) {
                    shouldSeparate = true;
                    break;
                }
            }

            if (shouldSeparate) {
                sb.append(separator(separator));
            }

            sb.append(renderLine(lines.get(i)));
        }

        return sb.toString();
    }

    /**
     * Appends to sb formatted value.
     * Trims string if its length > width.
     * @param align -1 for align left, 0 for center and +1 for align right.
     */
    private static void appendFormatted(StringBuilder sb, String value, int align, int width)
    {
        if (value.length() > width)
            value = value.substring(0,width);

        int before = (align == 0)
                ? (width - value.length()) / 2
                : (align == -1) ? 0 : width - value.length();

        int after = width - value.length() - before;

        while (before-- > 0)
            sb.append(" ");

        sb.append(value);

        while (after-- > 0)
            sb.append(" ");

        sb.append(" ");
    }
}

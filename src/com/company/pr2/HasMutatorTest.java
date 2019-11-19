package com.company.pr2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasMutatorTest {
    static class SomeClass implements HasMutator {
        int a;
        double b;
        String c;
    }

    @Test
    void getAttribute() throws NoSuchFieldException {
        SomeClass obj = new SomeClass();

        assertEquals("a", obj.getAttribute("a", obj.getClass()).getName());
        assertEquals(int.class, obj.getAttribute("a", obj.getClass()).getType());

        assertEquals("b", obj.getAttribute("b", obj.getClass()).getName());
        assertEquals(double.class, obj.getAttribute("b", obj.getClass()).getType());

        assertEquals("c", obj.getAttribute("c", obj.getClass()).getName());
        assertEquals(String.class, obj.getAttribute("c", obj.getClass()).getType());

        assertThrows(NoSuchFieldException.class, () -> obj.getAttribute("d", obj.getClass()));
    }

    @Test
    void setFieldValue() {
        SomeClass obj = new SomeClass();

        obj.setFieldValue("a", 5);
        assertEquals(5, obj.a);
        assertThrows(IllegalArgumentException.class, () -> obj.setFieldValue("a", "5"));

        obj.setFieldValue("c", "5");
        assertEquals("5", obj.c);
        assertThrows(IllegalArgumentException.class, () -> obj.setFieldValue("c", 5));

        assertThrows(IllegalArgumentException.class, () -> obj.setFieldValue("d", "5"));
    }

    @Test
    void getFieldValue() {
        SomeClass obj = new SomeClass();

        obj.a = 5;
        obj.c = "5";

        assertEquals(5, obj.getFieldValue("a"));
        assertEquals("5", obj.getFieldValue("c"));

        assertThrows(IllegalArgumentException.class, () -> obj.getFieldValue("d"));
    }
}
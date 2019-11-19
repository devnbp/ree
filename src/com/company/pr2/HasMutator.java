package com.company.pr2;

import java.lang.reflect.Field;

public interface HasMutator {
    /**
     * Trying to get class field or parent class field using recursion
     *
     * @param fieldName name of target field
     * @param clazz class reflection object
     * @return field object
     * @throws NoSuchFieldException when class have no such field
     */
    default Field getAttribute(String fieldName, Class<?> clazz) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            return getAttribute(fieldName, clazz.getSuperclass());
        }
        catch (NullPointerException e) {
            throw new NoSuchFieldException();
        }
    }


    /**
     * Trying to set data to target object field
     * @param fieldName name of target field
     * @param value data to set
     */
    default void setFieldValue(String fieldName, Object value) {
        try {
            Class<?> clazz = this.getClass();
            Field field = getAttribute(fieldName, clazz);
            String fieldType = field.getType().toString();
            if (!fieldType.equals("class java.lang.String")) {
                field.setAccessible(true);
                field.set(this, value);
            }
            else {
                fieldType = fieldType.split("\\s+")[1];
                if (value.getClass().getName().equals(fieldType)) {
                    field.setAccessible(true);
                    field.set(this, value);
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
        }
        catch (IllegalArgumentException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
        catch (SecurityException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Trying to get data of target object field
     * @param fieldName name of target field
     * @return actual field value
     */
    default Object getFieldValue(String fieldName) {
        try {
            Class<?> clazz = this.getClass();
            Field field = getAttribute(fieldName, clazz);

            field.setAccessible(true);
            return field.get(this);
        }
        catch (IllegalArgumentException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
        catch (SecurityException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.company.pr2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public interface HasValidator extends HasMutator {
    HashMap<String, HashMap<String, Object>> rules = new HashMap<>();
    boolean shouldValidate = true;

    /**
     * Extended {@link HasMutator#setFieldValue(String, Object)} by adding validation
     * @param fieldName name of target field
     * @param value data to set
     */
    default void setFieldValue(String fieldName, Object value) {
        if (shouldValidate) {
            boolean validated = validate(fieldName, value);

            if (!validated) {
                throw new IllegalArgumentException();
            }
        }

        HasMutator.super.setFieldValue(fieldName, value);
    }

    /**
     * Validating using declared object rules
     * @param field name of target field
     * @param value data to validate
     * @return is data passed validation
     */
    default boolean validate(String field, Object value) {
        HashMap<String, HashMap<String, Object>> thisRules = (HashMap<String, HashMap<String, Object>>) getFieldValue("rules");

        if (thisRules.containsKey(field)) {
            AtomicBoolean result = new AtomicBoolean(true);

            HashMap<String, Object> fieldRules = thisRules.get(field);

            Validation validator = new Validation();

            fieldRules.forEach((methodName, val) -> {
                try {
                    Method method = getValidationMethod(methodName);

                    if (!((boolean) method.invoke(validator, value, val, false))) {
                        result.set(false);
                    }
                }
                catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(e);
                }
                catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            return result.get();
        }

        return true;
    }

    /**
     * Trying to get {@link Validation} method
     * @param name of the method
     * @return method object
     * @throws NoSuchMethodException if Validation have no such public method
     */
    default Method getValidationMethod(String name) throws NoSuchMethodException {
        for (Method m : Validation.class.getMethods()) {
            if (name.equals(m.getName())) {
                return m;
            }
        }

        throw new NoSuchMethodException();
    }
}

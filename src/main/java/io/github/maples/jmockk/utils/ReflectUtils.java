package io.github.maples.jmockk.utils;

import java.lang.reflect.Field;

public class ReflectUtils {
    public static <T> T getInstance(Class<T> objectClass) {
        Field[] fields = objectClass.getFields();
        for (Field field : fields) {
            if (field.getType().equals(objectClass) && field.getName().equals("INSTANCE")) {
                try {
                    return (T) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}

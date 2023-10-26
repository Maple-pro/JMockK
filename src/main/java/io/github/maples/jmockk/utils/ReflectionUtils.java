package io.github.maples.jmockk.utils;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    /**
     * Get {@code INSTANCE} of the kotlin object class.
     *
     * @param objectClass the kotlin object class
     * @return the {@code INSTANCE} of the kotlin object class
     * @param <T> the type of the kotlin object class
     */
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

    public static Class<?>[] parameter2Types(Object[] parameters) {
        return Arrays.stream(parameters).map(arg -> {
            Class<?> parameterType = arg.getClass();
            if (ClassUtils.wrapperToPrimitive(parameterType) == null) {
                return parameterType;
            } else {
                return ClassUtils.wrapperToPrimitive(parameterType);
            }
        }).toArray(Class<?>[]::new);
    }

    public static Method getMethod(Class<?> targetClass, String methodName, Class<?>... parameterTypes) throws RuntimeException {
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {  // Check the method name
                Class<?>[] methodParameterTypes = method.getParameterTypes(); // Check the parameter types
                if (methodParameterTypes.length == parameterTypes.length) {
                    boolean isSame = true;
                    for (int i = 0; i < methodParameterTypes.length; i++) {
                        if (!ClassUtils.isAssignable(parameterTypes[i], methodParameterTypes[i])) {
                            isSame = false;
                            break;
                        }
                    }
                    if (isSame) {
                        return method;
                    }
                }
            }
        }

        throw new RuntimeException("No such method " + methodName + " in class " + targetClass.getName());
    }
}

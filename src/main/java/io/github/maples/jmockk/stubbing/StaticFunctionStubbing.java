package io.github.maples.jmockk.stubbing;

import io.github.maples.jmockk.Visibility;
import io.github.maples.jmockk.utils.ReflectUtils;
import io.mockk.MockKMatcherScope;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StaticFunctionStubbing extends Lambda implements Function1 {
    Class<?> staticClass;
    Boolean isObjectMethod;
    Visibility methodVisibility;
    String methodName;
    Object[] args;
    Class<?>[] parameterTypes;

    public StaticFunctionStubbing(
            Class<?> staticClass,
            Boolean isObjectMethod,
            Visibility methodVisibility,
            String methodName,
            Object... args
    ) {
        super(1);
        this.staticClass = staticClass;
        this.isObjectMethod = isObjectMethod;
        this.methodVisibility = methodVisibility;
        this.methodName = methodName;
        this.args = args;
        parameterTypes = Arrays.stream(args)
                .map(arg -> {
                    Class<?> parameterType = arg.getClass();
                    if (ClassUtils.wrapperToPrimitive(parameterType) == null) {
                        return parameterType;
                    } else {
                        return ClassUtils.wrapperToPrimitive(parameterType);
                    }
                })
                .toArray(Class<?>[]::new);
    }

    @Override
    public Object invoke(Object o) {
        MockKMatcherScope mockKMatcherScope = (MockKMatcherScope) o;
        return this.invoke(mockKMatcherScope);
    }

    public Object invoke(MockKMatcherScope mockKMatcherScope) {
        Intrinsics.checkNotNullParameter(mockKMatcherScope, "$this$every");
        try {
            switch (methodVisibility) {
                case PUBLIC, PROTECTED, PACKAGE -> {
                    if(isObjectMethod) {
                        Object instance = ReflectUtils.getInstance(staticClass);
                        if (instance == null) {
                            throw new RuntimeException("Cannot get instance of " + staticClass.getName());
                        }
                        return instance.getClass().getMethod(methodName, parameterTypes).invoke(instance, args);
                    } else {
                        return staticClass.getMethod(methodName, parameterTypes).invoke(null, args);
                    }
                }
                case PRIVATE -> {
                    Method method = staticClass.getDeclaredMethod(methodName, parameterTypes);
                    method.setAccessible(true);
                    return method.invoke(null, args);
                }
            };
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

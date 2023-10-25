package io.github.maples.jmockk.stubbing;

import io.github.maples.jmockk.Visibility;
import io.mockk.MockKMatcherScope;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class FunctionStubbing<T> extends Lambda implements Function1 {
    T mockObject;
    Visibility methodVisibility;
    String methodName;
    Object[] args;
    Class<?>[] parameterTypes;

    public FunctionStubbing(
            T mockObject,
            Visibility methodVisibility,
            String methodName,
            Object... args
    ) {
        super(1);
        this.mockObject = mockObject;
        this.methodVisibility = methodVisibility;
        this.methodName = methodName;
        this.args = args;
        parameterTypes = Arrays.stream(args)
                .map(arg -> {
                    Class<?> parameterType = arg.getClass();
                    return ClassUtils.wrapperToPrimitive(parameterType);
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
            return switch (methodVisibility) {
                case PUBLIC, PROTECTED, PACKAGE -> mockObject
                        .getClass()
                        .getMethod(methodName, parameterTypes)
                        .invoke(mockObject, args);
                case PRIVATE -> mockKMatcherScope.get(mockObject, methodName).invoke(args);
            };
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

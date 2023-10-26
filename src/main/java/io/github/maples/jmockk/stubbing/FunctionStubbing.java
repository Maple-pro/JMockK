package io.github.maples.jmockk.stubbing;

import io.github.maples.jmockk.Visibility;
import io.github.maples.jmockk.utils.ReflectionUtils;
import io.mockk.MockKMatcherScope;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        parameterTypes = ReflectionUtils.parameter2Types(args);
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
                case PUBLIC, PROTECTED, PACKAGE -> {
                    Class<?> targetClass = mockObject.getClass();
                    Method targetMethod = ReflectionUtils.getMethod(targetClass, methodName, parameterTypes);
                    yield targetMethod.invoke(mockObject, args);
                }
                case PRIVATE -> mockKMatcherScope.get(mockObject, methodName).invoke(args);
            };
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

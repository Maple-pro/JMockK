package io.github.maples.jmockk.stubbing;

import io.github.maples.jmockk.Visibility;
import io.mockk.MockKMatcherScope;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

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
        parameterTypes = Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
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
                case PUBLIC:
                case PROTECTED:
                case PACKAGE:
                    return mockObject
                            .getClass()
                            .getMethod(methodName, parameterTypes)
                            .invoke(mockObject, args);
                case PRIVATE:
                    return mockKMatcherScope.get(mockObject, methodName).invoke(args);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}

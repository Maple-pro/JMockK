package io.github.maples.jmockk.stubbing;

import io.mockk.MockKMatcherScope;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionStubbing<T> extends Lambda implements Function1 {
    T mockObject;
    Method methodToMock;
    Object[] args;

    public FunctionStubbing(T mockObject, Method methodToMock, Object... args) {
        super(1);
        this.mockObject = mockObject;
        this.methodToMock = methodToMock;
        this.args = args;
    }

    @Override
    public Object invoke(Object o) {
        MockKMatcherScope mockKMatcherScope = (MockKMatcherScope) o;
        return this.invoke(mockKMatcherScope);
    }

    public Object invoke(MockKMatcherScope mockKMatcherScope) {
        Intrinsics.checkNotNullParameter(mockKMatcherScope, "$this$every");
        try {
            return mockObject.getClass().getMethod(methodToMock.getName(), methodToMock.getParameterTypes()).invoke(mockObject, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

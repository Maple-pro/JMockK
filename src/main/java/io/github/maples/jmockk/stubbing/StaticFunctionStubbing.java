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
            if(isObjectMethod) {
                Object instance = ReflectionUtils.getInstance(staticClass);
                if (instance == null) {
                    throw new RuntimeException("Cannot get instance of " + staticClass.getName());
                }
                Method targetMethod = ReflectionUtils.getMethod(instance.getClass(), methodName, parameterTypes);
                if (methodVisibility == Visibility.PRIVATE) {
                    targetMethod.setAccessible(true);
                }
                return targetMethod.invoke(instance, args);
            } else {
                Method targetMethod = ReflectionUtils.getMethod(staticClass, methodName, parameterTypes);
                if (methodVisibility == Visibility.PRIVATE) {
                    targetMethod.setAccessible(true);
                }
                return targetMethod.invoke(null, args);
            }
        } catch (RuntimeException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

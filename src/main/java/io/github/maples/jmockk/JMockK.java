package io.github.maples.jmockk;

import io.github.maples.jmockk.stubbing.FunctionStubbing;
import io.github.maples.jmockk.stubbing.OngoingStubbing;
import io.mockk.MockKGateway;
import io.mockk.MockKStubScope;
import io.mockk.impl.JvmMockKGateway;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

import java.lang.reflect.Method;

import static io.mockk.MockKKt.every;

/**
 * A Java wrapper for MockK framework
 */
public class JMockK {

    private static MockKGateway.MockFactory getMockFactory() {
        Function0<JvmMockKGateway> implementationBuilder = JvmMockKGateway.Companion.getDefaultImplementationBuilder(); // r4
        MockKGateway.Companion.setImplementation(implementationBuilder);

        Function0<MockKGateway> mockKGatewayImplementation = MockKGateway.Companion.getImplementation(); // r7
        MockKGateway gateway = mockKGatewayImplementation.invoke(); // r8

        return gateway.getMockFactory();
    }

    /**
     * Replace {@code mockK()} in Kotlin with {@code MockKHelper.mockk() }
     * @param classToMock the class to be mocked
     * @return the mocked object
     * @param <T> the type of the class
     */
    public static <T> T mockk(Class<T> classToMock) {
        KClass[] kClasses = {};

        MockKGateway.MockFactory mockFactory = getMockFactory();
        KClass kClass = Reflection.getOrCreateKotlinClass(classToMock); // r11
        Object mockObject = mockFactory.mockk(kClass, null,true, kClasses, false); // r12

        return (T) mockObject;
    }

    public static <T> T spyk(Class<T> classToSpy) {
        KClass[] kClasses = {};

        MockKGateway.MockFactory mockFactory = getMockFactory();
        KClass kClass = Reflection.getOrCreateKotlinClass(classToSpy);
        Object spyObject = mockFactory.spyk(kClass, null, null, kClasses, false);

        return (T) spyObject;
    }

    // TODO
    public static <T> T mockkStatic(Class<T> staticClassToMock) {
        return null;
    }

    // TODO
    public static <T> void unmockkStatic(Class<T> staticClassToUnMock) {
    }

    // TODO
    public static <T> T mockkObject(Class<T> objectToMock) {
        return null;
    }

    // TODO
    public static <T> void unmockkObject(Class<T> objectToUnMock) {
    }

    // TODO
    public static <T> OngoingStubbing<T> when(
            Object mockObject,
            Visibility methodVisibility,
            String methodName,
            Object... args
    ) {
        Function1 stubBlock = new FunctionStubbing(mockObject, methodVisibility, methodName, args);

        MockKStubScope mockKStubScope = every(stubBlock);

        return new OngoingStubbing<>(mockKStubScope);
    }
}

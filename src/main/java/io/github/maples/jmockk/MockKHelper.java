package io.github.maples.jmockk;

import io.mockk.MockKGateway;
import io.mockk.impl.JvmMockKGateway;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

/**
 * A Java wrapper for MockK framework
 */
public class MockKHelper {

    /**
     * Replace {@code mockK()} in Kotlin with {@code MockKHelper.mockk() }
     * @param javaClass the class to be mocked
     * @return the mocked object
     * @param <T> the type of the class
     */
    public static <T> T mockk(Class<T> javaClass) {
        KClass[] kClasses = {};
        Function0<JvmMockKGateway> function0 = JvmMockKGateway.Companion.getDefaultImplementationBuilder(); // r4
        MockKGateway.Companion.setImplementation(function0);

        Function0<MockKGateway> function1 = MockKGateway.Companion.getImplementation(); // r7
        MockKGateway gateway = function1.invoke(); // r8

        MockKGateway.MockFactory mockFactory = gateway.getMockFactory(); // r10
        KClass kClass = Reflection.getOrCreateKotlinClass(javaClass); // r11
        Object mockObject = mockFactory.mockk(kClass, null,true, kClasses, false); // r12

        return (T) mockObject;
    }

}

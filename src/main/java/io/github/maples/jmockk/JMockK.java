package io.github.maples.jmockk;

import io.github.maples.jmockk.stubbing.FunctionStubbing;
import io.github.maples.jmockk.stubbing.OngoingStubbing;
import io.github.maples.jmockk.stubbing.StaticFunctionStubbing;
import io.github.maples.jmockk.utils.ReflectionUtils;
import io.mockk.MockKCancellationRegistry;
import io.mockk.MockKGateway;
import io.mockk.MockKKt;
import io.mockk.MockKStubScope;
import io.mockk.impl.JvmMockKGateway;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SpreadBuilder;
import kotlin.reflect.KClass;

import java.util.Arrays;
import java.util.List;

import static io.mockk.MockKKt.every;

/**
 * A Java wrapper for MockK framework
 */
public class JMockK {

    private static final MockKGateway mockKGateway = getMockKGateway();

    private static final MockKGateway.MockFactory mockFactory = mockKGateway.getMockFactory();
    private static final MockKGateway.StaticMockFactory staticMockFactory = mockKGateway.getStaticMockFactory();
    private static final MockKGateway.ObjectMockFactory objectMockFactory = mockKGateway.getObjectMockFactory();

    private static final MockKGateway.ClearOptions clearOptions = new MockKGateway.ClearOptions(
            true,
            true,
            true,
            true,
            true
    );

    private static MockKGateway getMockKGateway() {
        Function0<JvmMockKGateway> implementationBuilder = JvmMockKGateway.Companion.getDefaultImplementationBuilder();
        MockKGateway.Companion.setImplementation(implementationBuilder);

        Function0<MockKGateway> mockKGatewayImplementation = MockKGateway.Companion.getImplementation();
        return mockKGatewayImplementation.invoke();
    }

    /**
     * Builds a new mock for specified class.
     *
     * <p>A mock is a fake version of a class that replaces all the methods with fake implementations.
     *
     * <p>Replace {@code mockK()} in Kotlin with {@code MockKHelper.mockk() }
     *
     * @param classToMock the class to be mocked
     * @param relaxed allows creation with no specific behaviour. Unstubbed methods will not throw.
     * @return the mocked object
     * @param <T> the type of the class
     */
    public static <T> T mockk(
            Class<T> classToMock,
            boolean relaxed
    ) {
        KClass<?>[] kClasses = {};

        KClass<?> kClass = Reflection.getOrCreateKotlinClass(classToMock); // r11
        Object mockObject = mockFactory.mockk(kClass, null, relaxed, kClasses, false); // r12

        return (T) mockObject;
    }

    /**
     * Builds a new spy for specified class. Initializes object via default constructor.
     *
     * <p>A spy is a special kind of [mockk] that enables a mix of mocked behaviour and real behaviour.
     * A part of the behaviour may be mocked using [every], but any non-mocked behaviour will call the original method.
     *
     * @param classToSpy the class to be spied
     * @return the spied object
     * @param <T> the type of the class
     */
    public static <T> T spyk(Class<T> classToSpy) {
        KClass<?> kClass = Reflection.getOrCreateKotlinClass(classToSpy);
        KClass<?>[] kClasses = {};

        Object spyObject = mockFactory.spyk(kClass, null, null, kClasses, false);

        return (T) spyObject;
    }

    public static <T> T spyk(T objectToSpy) {
        KClass<?>[] kClasses = {};
        Object spyObject = mockFactory.spyk(null, objectToSpy, null, kClasses, false);

        return (T) spyObject;
    }

    /**
     * Build a static mock. Any mock of this exact class are cancelled before it's mocked.
     */
    @SafeVarargs
    public static <T> void mockkStatic(Class<T>... staticClassesToMock) {
        KClass<?>[] kClasses = Arrays.stream(staticClassesToMock)
                .map(Reflection::getOrCreateKotlinClass)
                .toArray(KClass[]::new);

        for (KClass<?> kClass : kClasses) {
            Function0<Unit> mockkFunction = staticMockFactory.staticMockk(kClass);
            staticMockFactory.clear(kClass, clearOptions);
            MockKCancellationRegistry.INSTANCE
                    .subRegistry(MockKCancellationRegistry.Type.STATIC)
                    .cancelPut(kClass, mockkFunction);
        }
    }

    // TODO
    public static <T> void unmockkStatic(Class<T> staticClassToUnMock) {
    }

    /**
     * Builds an Object mock. Any mocks of this exact object are cancelled before it's mocked.
     *
     * <p>This lets you mock object methods with [every]
     */
    @SafeVarargs
    public static <T> void mockkObject(Class<T>... objectsToMock) {
        List<T> instances = Arrays.stream(objectsToMock).map(ReflectionUtils::getInstance).toList();

        for (T instance : instances) {
            Function0 mockkFunction = objectMockFactory.objectMockk(instance, false);

            MockKGateway.Clearer clearer = mockKGateway.getClearer();
            SpreadBuilder spreadBuilder = new SpreadBuilder(2);
            spreadBuilder.add(instance);
            spreadBuilder.addSpread(new Object[]{});
            int size = spreadBuilder.size();
            Object[] array = new Object[size];
            Object[] newArray = spreadBuilder.toArray(array);

            clearer.clear(newArray, clearOptions);

            MockKCancellationRegistry.INSTANCE
                    .subRegistry(MockKCancellationRegistry.Type.OBJECT)
                    .cancelPut(instance, mockkFunction);
        }
    }

    // TODO
    public static <T> void unmockkObject(Class<T> objectToUnMock) {
    }

    /**
     * Mock member method
     *
     * @param mockObject the object to be mocked
     * @param methodVisibility the visibility of the method
     * @param methodName the name of the method
     * @param args the arguments of the method call
     */
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

    /**
     * Mock static method
     *
     * @param staticClass the static class to be mocked
     * @param methodVisibility the visibility of the static method
     * @param methodName the name of the static method
     * @param args the arguments of the static method call
     */
    public static <T> OngoingStubbing<T> whenStatic(
            Class<?> staticClass,
            Boolean isObjectMethod,
            Visibility methodVisibility,
            String methodName,
            Object... args
    ) {
        Function1 stubBlock = new StaticFunctionStubbing(staticClass, isObjectMethod, methodVisibility, methodName, args);
        MockKStubScope mockKStubScope = MockKKt.every(stubBlock);
        return new OngoingStubbing<>(mockKStubScope);
    }
}

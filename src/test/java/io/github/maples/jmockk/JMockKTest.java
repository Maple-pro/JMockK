package io.github.maples.jmockk;

import org.junit.Assert;
import org.junit.Test;

import static io.github.maples.jmockk.JMockK.*;

public class JMockKTest {
    @Test
    public void testMockk() {
        Foo foo = mockk(Foo.class);
        int result1 = foo.foo();
        boolean result2 = foo.bar();

        System.out.println(result1);
        System.out.println(result2);

        Assert.assertEquals(0, result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testSpyk() {
        Foo foo = spyk(Foo.class);
        int result = foo.foo();
        System.out.println(result);

        Assert.assertEquals(1, result);
    }

    @Test
    public void testMockkStatic() {

    }

    @Test
    public void testunmockStatic() {

    }

    @Test
    public void testMockkObject() {

    }

    @Test
    public void testUnmockkObject() {

    }


    @Test
    public void testWhenPublic() throws NoSuchMethodException {
        Foo foo = spyk(Foo.class);
        when(foo, Visibility.PUBLIC, "foo").thenReturn(3);
        System.out.println(foo.foo());
    }

    @Test
    public void testWhenPrivate() throws NoSuchMethodException {
        Foo foo = spyk(Foo.class);
        when(foo, Visibility.PRIVATE, "test").thenReturn(false);
        System.out.println(foo.foo());
    }

}

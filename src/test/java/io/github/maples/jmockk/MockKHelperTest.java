package io.github.maples.jmockk;

import org.junit.Assert;
import org.junit.Test;

public class MockKHelperTest {
    @Test
    public void testMockK() {
        Foo foo = MockKHelper.mockk(Foo.class);
        int result1 = foo.foo();
        boolean result2 = foo.bar();

        System.out.println(result1);
        System.out.println(result2);

        Assert.assertEquals(0, result1);
        Assert.assertFalse(result2);
    }
}

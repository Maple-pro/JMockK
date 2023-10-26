package io.github.maples.jmockk;

import org.junit.Assert;
import org.junit.Test;

import static io.github.maples.jmockk.JMockK.*;

public class JMockKTest {
    @Test
    public void testMockk() {
        Foo foo = mockk(Foo.class, true);
        int result1 = foo.foo();
        boolean result2 = foo.bar();

        Assert.assertEquals(0, result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testSpyk() {
        Foo foo = spyk(new Foo(1));
        int result = foo.foo();

        Assert.assertEquals(2, result);
    }

    @Test
    public void testSpykObject() {
        Foo foo = spyk(new Foo(10));
        mockkStatic(Foo.class);
        whenStatic(Foo.class, false, Visibility.PRIVATE, "privateStaticMethod").thenReturn(false);
        int res = foo.printA();
        Assert.assertEquals(9, res);
    }

    @Test
    public void testMockkStatic() {
        mockkStatic(Foo.class);
        mockkStatic(KotlinObject.class);
    }

    @Test
    public void testUnmockStatic() {

    }

    @Test
    public void testMockkObject() {
        mockkObject(KotlinObject.class);
        whenStatic(KotlinObject.class, true, Visibility.PUBLIC, "bar").thenReturn(false);
        boolean res = KotlinObject.INSTANCE.bar();
        Assert.assertFalse(res);
    }

    @Test
    public void testUnmockkObject() {

    }

    @Test
    public void testMockkInterface() {
        Person person = mockk(Person.class, true);
        when(person, Visibility.PUBLIC, "getName").thenReturn("maples");
        Assert.assertEquals("maples", person.getName());
    }

    @Test
    public void testSpykInterface() {
        Person person = spyk(Person.class);
        when(person, Visibility.PUBLIC, "getName").thenReturn("maples");
        Assert.assertEquals("maples", person.getName());
    }


    @Test
    public void testWhenPublic() {
        Foo foo = spyk(new Foo(1));
        when(foo, Visibility.PUBLIC, "foo").thenReturn(3);
        when(foo, Visibility.PUBLIC, "bar").thenReturn(false);
        Assert.assertEquals(3, foo.foo());
        Assert.assertFalse(foo.bar());
    }

    @Test
    public void testWhenPrivate() {
        Foo foo = spyk(new Foo(1));
        when(foo, Visibility.PRIVATE, "test").thenReturn(true);
        int res = foo.foo();
        Assert.assertEquals(1, res);
    }

    @Test
    public void testWhenStatic() {
        mockkStatic(Foo.class);
        whenStatic(Foo.class, false, Visibility.PUBLIC, "staticMethod", new int[] { 1 }).thenReturn(0);

        int res1 = Foo.staticMethod(new int[] { 1 });
        int res2 = Foo.staticMethod(new int[] { });

        Assert.assertEquals(0, res1);
        Assert.assertEquals(111111, res2);
    }

    @Test
    public void testWhenPrivateStatic() {
        mockkStatic(Foo.class);
        whenStatic(Foo.class, false, Visibility.PRIVATE, "privateStaticMethod").thenReturn(false);
        int res = Foo.staticMethod(new int[] {});

        Assert.assertEquals(114514, res);
    }

    @Test
    public void testKotlinClass() {
        KotlinClass kotlinClass = spyk(KotlinClass.class);
        when(kotlinClass, Visibility.PUBLIC, "bar", 2).thenReturn(false);

        int res = kotlinClass.foo(10);
        Assert.assertEquals(11, res);
    }

    @Test
    public void testComplexType() {
        KotlinClass kotlinClass = spyk(KotlinClass.class);

        Person person = mockk(Person.class, true); // parameter
        Person returnPerson = mockk(Person.class, true); // return

        when(returnPerson, Visibility.PUBLIC, "getName").thenReturn("yangfeng");
        Assert.assertEquals("yangfeng", returnPerson.getName());

        when(kotlinClass, Visibility.PUBLIC, "getPerson", person).thenReturn(returnPerson);

        Person res = kotlinClass.getPerson(person);
        Assert.assertEquals("yangfeng", res.getName());
    }

}

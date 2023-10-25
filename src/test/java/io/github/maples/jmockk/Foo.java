package io.github.maples.jmockk;

public class Foo {
    int a;

    public Foo(int a) {
        this.a = a;
    }

    public int printA() {
        if (privateStaticMethod()) {
            return a + 1;
        } else {
            return a - 1;
        }
    }

    public int foo() {
        System.out.println("Foo.foo");
        if (test()) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean bar() {
        System.out.println("Foo.bar");
        return true;
    }

    private boolean test() {
        System.out.println("Foo.test");
        return false;
    }

    public static int staticMethod(int[] params) {
        if (privateStaticMethod()) {
            return 111111;
        } else {
            return 114514;
        }
    }

    private static boolean privateStaticMethod() {
        return true;
    }

    public void test2(Foo foo) {

    }
}

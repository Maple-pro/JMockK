package io.github.maples.jmockk;

public class Foo {
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
}

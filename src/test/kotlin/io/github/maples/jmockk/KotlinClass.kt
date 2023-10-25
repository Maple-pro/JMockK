package io.github.maples.jmockk

class KotlinClass {
    fun foo(param: Int): Int {
        return if (bar(1)) {
            param + 1
        } else {
            param - 1
        }
    }

    fun bar(param: Int): Boolean {
        return true
    }
}
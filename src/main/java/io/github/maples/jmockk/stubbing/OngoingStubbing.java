package io.github.maples.jmockk.stubbing;

import io.mockk.MockKStubScope;

public class OngoingStubbing<T> {
    MockKStubScope mockKStubScope;

    public OngoingStubbing(MockKStubScope mockKStubScope) {
        this.mockKStubScope = mockKStubScope;
    }

    public void thenReturn(T value) {
        mockKStubScope.returns(value);
    }
}

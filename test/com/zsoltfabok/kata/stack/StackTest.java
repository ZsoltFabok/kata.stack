package com.zsoltfabok.kata.stack;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StackTest {

    private Stack stack;

    @Before
    public void setUp() {
        stack = new Stack();
    }

    @Test
    public void shouldPopTheLastPushedElement() {
        stack.push(3);
        assertEquals(3, stack.pop());
    }

    @Test
    public void shouldPopStackedElementsInReverseOrder() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void shouldReturnTheCurrentSizeOfTheStack() {
        for (int i = 0; i < 3; i++) {
            stack.push(i);
        }
        assertEquals(3, stack.size());
    }

    @Test
    public void shouldHaveAllTheElementsAfterIncreasingInternalCapacity() {
        for (int i = 0; i < 11; i++) {
            stack.push(i);
        }

        for (int i = 10; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
    }
}

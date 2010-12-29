package com.zsoltfabok.kata.stack;

public class Stack {

    private int[] container = new int[10];
    private int nextIndex = 0;

    public void push(int value) {
        if (isContainerFull()) {
            enlargeContainerCapacity();
        }
        container[nextIndex++] = value;
    }

    public int pop() {
        return container[--nextIndex];
    }

    public int size() {
        return nextIndex;
    }

    private boolean isContainerFull() {
        return nextIndex == container.length;
    }

    private void enlargeContainerCapacity() {
        int[] extentedContainer = new int[container.length + 10];
        System.arraycopy(container, 0, extentedContainer, 0, container.length);
        container = extentedContainer;
    }
}

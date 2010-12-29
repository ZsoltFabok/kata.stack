package com.zsoltfabok.kata.stack.concurrent;

public class SystemWrapper {
    public void printf(String message, Object... objects) {
        System.out.printf(message, objects);
    }
}

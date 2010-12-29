package com.zsoltfabok.kata.stack.concurrent;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.zsoltfabok.kata.stack.Stack;


public class Emitter implements Runnable {

    private final Stack stack;
    private final Lock lock;
    private final Condition condition;
    private final Random random;
    private final ThreadWrapper threadWrapper;

    public Emitter(Stack stack, Lock lock, Condition condition, Random random,
            ThreadWrapper threadWrapper) {
        this.stack = stack;
        this.lock = lock;
        this.condition = condition;
        this.random = random;
        this.threadWrapper = threadWrapper;
    }

    @Override
    public void run() {
        boolean lastPush = false;
        while (!lastPush) {
            if (lock.tryLock()) {
                try {
                    lastPush = pushRandomAmountOfValuesOnTheStack();
                    condition.signal();
                } finally {
                    lock.unlock();
                }
            }
            sleepForRandomAmountOfSeconds();
        }
    }

    private boolean pushRandomAmountOfValuesOnTheStack() {
        int value = 0;
        while (random.nextBoolean() && value != -1) {
            value = random.nextInt(11) - 1;
            stack.push(value);
        }
        return value == -1;
    }

    private void sleepForRandomAmountOfSeconds() {
        threadWrapper.sleep(random.nextInt(2) + 1);
    }
}

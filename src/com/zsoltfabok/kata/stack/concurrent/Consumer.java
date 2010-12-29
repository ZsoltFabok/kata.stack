package com.zsoltfabok.kata.stack.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.zsoltfabok.kata.stack.Stack;

public class Consumer implements Runnable {

    private final Stack stack;
    private final Lock lock;
    private final Condition condition;
    private final SystemWrapper systemWrapper;

    public Consumer(Stack stack, Lock lock, Condition condition,
            SystemWrapper systemWrapper) {
        this.stack = stack;
        this.lock = lock;
        this.condition = condition;
        this.systemWrapper = systemWrapper;
    }

    @Override
    public void run() {
        boolean terminationArrives = false;
        while (!terminationArrives) {
            if (lock.tryLock()) {
                try {
                    List<Integer> values = new ArrayList<Integer>();
                    if (stack.size() > 0) {
                        values = popAllValuesFromStack();
                        double average = average(values);
                        systemWrapper.printf("values: %s and the average %.2f%n",
                                values, average);
                    }

                    if (containsTermination(values)) {
                        terminationArrives = true;
                    } else {
                        condition.awaitUninterruptibly();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private List<Integer> popAllValuesFromStack() {
        List<Integer> values = new ArrayList<Integer>();
        while (stack.size() > 0) {
            values.add(stack.pop());
        }
        return values;
    }

    private boolean containsTermination(List<Integer> values) {
        return values.contains(new Integer(-1));
    }

    private double average(List<Integer> values) {
        int sum = 0;
        int numberOfTerminators = 0;
        for (Integer value : values) {
            if (value > 0) {
                sum += value;
            } else {
                numberOfTerminators++;
            }
        }
        return (double) sum / (values.size() - numberOfTerminators);
    }
}

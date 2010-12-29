package com.zsoltfabok.kata.stack.concurrent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zsoltfabok.kata.stack.Stack;

public class Main {

    public static void main(String[] args) {
        Stack stack = new Stack();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Random random = new Random();
        SystemWrapper systemWrapper = new SystemWrapper();
        ThreadWrapper threadWrapper = new ThreadWrapper();

        Emitter emitter = new Emitter(stack, lock, condition, random, threadWrapper);
        Consumer consumer = new Consumer(stack, lock, condition, systemWrapper);

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(emitter);
        service.execute(consumer);
        service.shutdown();
    }
}

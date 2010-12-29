package com.zsoltfabok.kata.stack.concurrent;

public class ThreadWrapper {
    public void sleep(int seconds) {
     try {
         Thread.sleep(seconds * 1000L);
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
    }
}

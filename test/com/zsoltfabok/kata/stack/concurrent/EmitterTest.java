package com.zsoltfabok.kata.stack.concurrent;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.zsoltfabok.kata.stack.Stack;

@RunWith(MockitoJUnitRunner.class)
public class EmitterTest {

    /* @formatter:off */
    @Mock private Stack stack;
    @Mock private Lock lock;
    @Mock private Condition condition;
    @Mock private Random random;
    @Mock private ThreadWrapper threadWrapper;
    /* @formatter:on */

    private Emitter emitter;

    @Before
    public void setUp() {
        emitter = new Emitter(stack, lock, condition, random, threadWrapper);
    }

    @Test
    public void shouldPutTwoValuesOnTheStackAndFinish() {
        when(lock.tryLock()).thenReturn(true);
        when(random.nextBoolean()).thenReturn(true, true, true);
        when(random.nextInt(11)).thenReturn(4, 6, 0);
        when(random.nextInt(2)).thenReturn(1);

        emitter.run();

        verify(condition).signal();
        verify(lock).unlock();
        verify(threadWrapper).sleep(2);

        verify(stack).push(3);
        verify(stack).push(5);
        verify(stack).push(-1);
    }

    @Test
    public void shouldDoThePushingTwiceAndThenFinish() {
        when(lock.tryLock()).thenReturn(true, true);
        when(random.nextBoolean()).thenReturn(true, false, true, true);
        when(random.nextInt(11)).thenReturn(4, 4, 0);
        when(random.nextInt(2)).thenReturn(1, 1);

        emitter.run();

        verify(condition, times(2)).signal();
        verify(lock, times(2)).unlock();
        verify(threadWrapper, times(2)).sleep(2);

        verify(stack, times(2)).push(3);
        verify(stack).push(-1);
    }
}

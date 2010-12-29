package com.zsoltfabok.kata.stack.concurrent;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.zsoltfabok.kata.stack.Stack;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerTest {

    /* @formatter:off */
    @Mock private Stack stack;
    @Mock private Lock lock;
    @Mock private Condition condition;
    @Mock private SystemWrapper systemWrapper;
    /* @formatter:on */
    private Consumer consumer;

    @Before
    public void setUp() {
        consumer = new Consumer(stack, lock, condition, systemWrapper);
    }

    @Test
    public void shouldPrintAverageOfTwoStackValuesAndThenFinish() {
        when(lock.tryLock()).thenReturn(true);
        when(stack.size()).thenReturn(3, 3, 2, 1, 0);
        when(stack.pop()).thenReturn(3, 5, -1);

        consumer.run();

        List<Integer> values = new ArrayList<Integer>();
        values.add(3);
        values.add(5);
        values.add(-1);
        verify(systemWrapper).printf("values: %s and the average %.2f%n",
                values, new Double(4));

        verify(lock).unlock();
    }

    @Test
    public void shouldDoThePoppingTwiceAndThenFinish() {
        when(lock.tryLock()).thenReturn(true, true);
        when(stack.size()).thenReturn(1, 1, 0, 2, 2, 1, 0);
        when(stack.pop()).thenReturn(3, 5, -1);

        consumer.run();

        List<Integer> values = new ArrayList<Integer>();
        values.add(3);
        verify(systemWrapper).printf("values: %s and the average %.2f%n",
                values, new Double(3));

        List<Integer> valuesSecondRound = new ArrayList<Integer>();
        valuesSecondRound.add(5);
        valuesSecondRound.add(-1);
        verify(systemWrapper).printf("values: %s and the average %.2f%n",
                valuesSecondRound, new Double(5));

        verify(lock, times(2)).unlock();
        verify(condition).awaitUninterruptibly();
    }
}

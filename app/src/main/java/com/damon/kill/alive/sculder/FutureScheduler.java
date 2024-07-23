package com.damon.kill.alive.sculder;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;


public interface FutureScheduler {
    ScheduledFuture<?> scheduleFuture(Runnable command, long millisecondDelay);
    ScheduledFuture<?> scheduleFutureWithFixedDelay(Runnable command,
                                                    long initialMillisecondDelay,
                                                    long millisecondDelay);
    <V> ScheduledFuture<V> scheduleFutureWithReturn(Callable<V> callable, long millisecondDelay);

    void teardown();
}

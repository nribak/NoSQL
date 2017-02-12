package org.ribak.nosql.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by nribak on 20/11/2016.
 * A {@link ThreadPoolExecutor} with a custom {@link PriorityBlockingQueue} that compares the priority of each transaction
 */

public class PriorityPoolExecutor extends ThreadPoolExecutor
{
    public PriorityPoolExecutor()
    {
        super(1, 1, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
    {
        PriorityCallable<T> priorityCallable = (PriorityCallable<T>) callable;
        return new PriorityFutureTask<>(priorityCallable, priorityCallable.getModule(), priorityCallable.getQueuePriority());
    }

    private class PriorityFutureTask <V> extends FutureTask <V> implements Comparable<PriorityFutureTask<V>>
    {
        private QueuePriority queuePriority;
        private String module;
        private PriorityFutureTask(Callable<V> callable, String module, QueuePriority queuePriority)
        {
            super(callable);
            this.queuePriority = queuePriority;
            this.module = module;
        }

        @Override
        public int compareTo(@NonNull PriorityFutureTask<V> another) {
            Integer lPriority = this.queuePriority.getPriority();
            Integer rPriority = another.queuePriority.getPriority();

            int priorityCompare = lPriority.compareTo(rPriority);
            if(priorityCompare != 0)
                return priorityCompare;
            return this.module.compareTo(another.module);
        }
    }
}

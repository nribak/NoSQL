package org.ribak.nosql.utils;

import java.util.concurrent.Callable;

/**
 * Created by nribak on 20/11/2016.
 * A {@link Callable} class with extra info
 */

public abstract class PriorityCallable <V> implements Callable <V>
{
    private QueuePriority mQueuePriority;
    private String mModule;

    protected PriorityCallable(String module, QueuePriority queuePriority)
    {
        mQueuePriority = queuePriority;
        mModule = module;
    }

    QueuePriority getQueuePriority()
    {
        return mQueuePriority;
    }

    String getModule()
    {
        return mModule;
    }

    @Override
    public V call() throws Exception
    {
        onPreCall();
        V result = runCall();
        onPostCall(result);
        return result;
    }

    protected void onPreCall()
    {
        // empty body
    }

    protected abstract V runCall() throws Exception;

    protected void onPostCall(V result)
    {
        // empty body
    }
}

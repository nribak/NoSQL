package org.ribak.nosql.utils;

/**
 * Created by nribak on 20/11/2016.
 */

public enum QueuePriority
{
    low (30),
    medium (20),
    high (10);

    private int priority;

    QueuePriority(int priority)
    {
        this.priority = priority;
    }

    public int getPriority()
    {
        return priority;
    }
}

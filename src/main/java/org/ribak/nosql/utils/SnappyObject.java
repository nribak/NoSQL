package org.ribak.nosql.utils;

/**
 * Created by nribak on 16/11/2016.
 */

public class SnappyObject <T>
{
    private T object;
    private boolean objectNull;

    public SnappyObject()
    {
    }

    public SnappyObject(T object)
    {
        this.object = object;
        this.objectNull = (object == null);
    }

    public boolean isObjectNull() {
        return objectNull;
    }

    public T getObject()
    {
        if(objectNull)
            return null;
        return object;
    }

    @Override
    public String toString()
    {
        if(object != null)
            return object.toString();
        return "NULL";
    }
}

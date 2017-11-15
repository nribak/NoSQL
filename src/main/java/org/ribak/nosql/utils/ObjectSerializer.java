package org.ribak.nosql.utils;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by nribak on 24/03/2017.
 */

public interface ObjectSerializer <T> {
    void write(T object, Output output);
    T read(Class<T> cls, Input input);
    Class<T> getSerializerClass();


}

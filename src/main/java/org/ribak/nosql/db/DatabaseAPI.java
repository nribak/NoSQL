package org.ribak.nosql.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.intellij.lang.annotations.RegExp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by nribak on 11/03/2018.
 */

public abstract class DatabaseAPI {
    private IDBStreamApi streamApi;

    DatabaseAPI(String module) {
        streamApi = createStreamApi(module);
    }

    @NonNull
    abstract IDBStreamApi createStreamApi(String module);

    abstract void write(OutputStream outputStream, @Nullable Object value);

    @Nullable
    abstract Object read(InputStream inputStream);

    public void put(@NonNull String key, @Nullable Object value) throws IOException {
        OutputStream outputStream = streamApi.getOutputStream(key);
        write(outputStream, value);
        streamApi.close(outputStream);
    }

    @Nullable
    public Object get(@NonNull String key) throws IOException {
        InputStream inputStream = streamApi.getInputStream(key);
        Object value = read(inputStream);
        streamApi.close(inputStream);
        return value;
    }

    public abstract boolean supportsBatching();

    public void putBatch(@NonNull String key, Object... objects) throws IOException {
        if(supportsBatching()) {
            OutputStream outputStream = streamApi.getOutputStream(key);
            int count = objects.length;
            write(outputStream, count);
            for (Object object : objects)
                write(outputStream, object);
        }
    }

    public List<Object> getBatch(@NonNull String key) throws IOException {
        List<Object> list = new ArrayList<>();
        if(supportsBatching()) {
            InputStream inputStream = streamApi.getInputStream(key);
            Object count = read(inputStream);
            if(count != null && count instanceof Integer)
                for (int i = 0; i < (int) count; i++)
                    list.add(read(inputStream));
        }
        return list;
    }

    public boolean contains(@NonNull String key) {
        return streamApi.keyExists(key);
    }

    public boolean delete(@NonNull String key) {
        return streamApi.removeKey(key);
    }

    public boolean destroy() {
        return streamApi.deleteAll();
    }

    public Set<String> getKeys(@Nullable String keyPrefix) {
        @RegExp String regex = "^" + keyPrefix + ".+";
        return streamApi.getKeys(regex);
    }
}

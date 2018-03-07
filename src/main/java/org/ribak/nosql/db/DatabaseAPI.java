package org.ribak.nosql.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by nribak on 06/03/2018.
 */

public interface DatabaseAPI {

    void put(@NonNull String key, @Nullable Object value) throws IOException;

    @Nullable
    Object get(@NonNull String key) throws IOException;

    boolean supportsBatching();

    void putBatch(@NonNull String key, final Object... objects) throws IOException;

    List<Object> getBatch(@NonNull String key) throws IOException;

    boolean contains(@NonNull String key);

    boolean delete(@NonNull String key);

    boolean destroy();

    Set<String> getKeys(@Nullable String keyPrefix);

}

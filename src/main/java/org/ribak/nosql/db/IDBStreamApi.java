package org.ribak.nosql.db;

import android.support.annotation.NonNull;

import org.intellij.lang.annotations.RegExp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by nribak on 10/03/2018.
 */

public interface IDBStreamApi {

    InputStream getInputStream(String key) throws IOException;

    OutputStream getOutputStream(String key) throws IOException;

    boolean keyExists(String key);

    boolean removeKey(String key);

    boolean deleteAll();

    Set<String> getKeys(@RegExp @NonNull String regex);

    default void close(Closeable stream) {
        if(stream != null)
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

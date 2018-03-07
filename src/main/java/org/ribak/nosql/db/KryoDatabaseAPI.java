package org.ribak.nosql.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.ribak.nosql.utils.KryoFactory;
import org.ribak.nosql.utils.exceptions.IllegalDirectoryException;
import org.ribak.nosql.utils.exceptions.InitializationException;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nribak on 06/03/2018.
 */

public class KryoDatabaseAPI implements DatabaseAPI {

    private static final String ROOT_DIR = "Kryo-DB";

    private static File ROOT_DIRECTORY;

    public static void init(Context context) {
        ROOT_DIRECTORY = new File(context.getFilesDir(), ROOT_DIR);
    }

    private File folder;
    private Kryo kryo;

    KryoDatabaseAPI(String moduleName) {
        if(ROOT_DIRECTORY == null)
            throw new InitializationException("init() was never called");
        kryo = KryoFactory.newInstance();
        if(ROOT_DIRECTORY.exists() || ROOT_DIRECTORY.mkdir())
            folder = new File(ROOT_DIRECTORY, moduleName);
        if(folder == null || (!folder.exists() && !folder.mkdir()))
            throw new IllegalDirectoryException(folder);
    }

    @Override
    public void put(@NonNull String key, @Nullable Object value) throws IOException {
        Output output = getKryoOutput(key);
        kryo.writeClassAndObject(output, value);
        closeKryoStream(output);
    }

    @Nullable
    @Override
    public Object get(@NonNull String key) throws IOException {
        Input input = getKryoInput(key);
        Object value = kryo.readClassAndObject(input);
        closeKryoStream(input);
        return value;
    }

    @Override
    public boolean supportsBatching() {
        return true;
    }

    @Override
    public void putBatch(@NonNull String key, Object... objects) throws IOException {
        Output output = getKryoOutput(key);
        for (Object object : objects)
            kryo.writeClassAndObject(output, object);
        closeKryoStream(output);
    }

    @Override
    public List<Object> getBatch(@NonNull String key) throws IOException {
        List<Object> list = new ArrayList<>();
        Input input = getKryoInput(key);
        while (!input.eof())
            list.add(kryo.readClassAndObject(input));
        closeKryoStream(input);
        return list;
    }

    @Override
    public boolean contains(@NonNull String key) {
        File file = Translator.getFile(folder, key);
        return file.exists();
    }

    @Override
    public boolean delete(@NonNull String key) {
        File file = Translator.getFile(folder, key);
        return file.exists() && file.delete();
    }

    @Override
    public boolean destroy() {
        boolean success = true;
        File[] files = folder.listFiles();
        for (File file : files)
            success = success && file.delete();
        return success;
    }

    @Override
    public Set<String> getKeys(@Nullable String keyPrefix) {
        File[] allFiles = folder.listFiles();
        Set<String> keys = new HashSet<>();
        for (File file : allFiles)
            if(keyPrefix == null || file.getName().startsWith(keyPrefix))
                keys.add(Translator.getKey(file));
        return Collections.unmodifiableSet(keys);
    }

    private static class Translator {
        private static final String SUFFIX = ".kryo";
        private static File getFile(File folder, String key) {
            return new File(folder, key + SUFFIX);
        }

        private static String getKey(File file) {
            String name = file.getName();
            return name.substring(0, name.length() - SUFFIX.length());
        }
    }

    private Input getKryoInput(String key) throws IOException {
        return new Input(new FileInputStream(Translator.getFile(folder, key)));
    }

    private Output getKryoOutput(String key) throws IOException {
        return new Output(new FileOutputStream(Translator.getFile(folder, key)));
    }

    private void closeKryoStream(Closeable stream) {
        if(stream != null)
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

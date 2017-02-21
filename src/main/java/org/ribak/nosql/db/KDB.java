package org.ribak.nosql.db;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import org.ribak.nosql.utils.exceptions.DBDestroyedException;
import org.ribak.nosql.utils.exceptions.IllegalDirectoryException;
import org.ribak.nosql.utils.exceptions.InitializationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nribak on 21/02/2017.
 */

public class KDB {
    private static final String ROOT_DIR = "Kryo-DB";

    private static File ROOT_DIRECTORY;

    public static void init(Context context) {
        ROOT_DIRECTORY = new File(context. getFilesDir(), ROOT_DIR);
    }

    private File folder;
    private Kryo kryo;
    private boolean dead;

    KDB(String moduleName) {
        if(ROOT_DIRECTORY == null)
            throw new InitializationException("init() was never called");
        dead = false;
        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        if(ROOT_DIRECTORY.exists() || ROOT_DIRECTORY.mkdir())
            folder = new File(ROOT_DIRECTORY, moduleName);
        if(folder == null || (!folder.exists() && !folder.mkdir()))
            throw new IllegalDirectoryException(folder);

    }

    private static final String SEPARATOR = ":";
    private static final String SUFFIX = ".kryo";
    private File getFile(String key) {
        File file = folder;
        String[] parts = key.split(SEPARATOR);
        for (int i = 0; i < parts.length; i++) {
            if(i == parts.length - 1)
                file = new File(file.getPath(), parts[i] + SUFFIX);
            else {
                file = new File(file.getPath(), parts[i]);
                if(!file.exists() && !file.mkdir())
                    throw new IllegalDirectoryException(file);
            }
        }
        return file;
    }

    private String getKey(File file) {
        StringBuilder builder = new StringBuilder();
        boolean b = false;
        for (String part : file.getPath().split("/")) {
            if(b) {
                if(builder.length() > 0)
                    builder.append(SEPARATOR);
                builder.append(part);
            }
            if(part.equals(folder.getName()))
                b = true;
        }
        int length = builder.length();
        return builder.delete(length - SUFFIX.length(), length).toString();
    }

    private List<File> getFiles(File directory) {
        List<File> files = new ArrayList<>();

        File[] f = directory.listFiles();
        for (File file : f) {
            if(file.isDirectory())
                files.addAll(getFiles(file));
            else
                files.add(file);
        }
        return files;
    }

    public void put(String key, final Object object) throws IOException {
        new OutputStreamFactory(key){

            @Override
            protected Void performTransaction(Output stream) {
                kryo.writeClassAndObject(stream, object);
                return null;
            }
        }.run();
    }

    public Object get(String key) throws IOException {
        return new InputStreamFactory<Object>(key) {

            @Override
            protected Object performTransaction(Input input) {
                return kryo.readClassAndObject(input);
            }
        }.run();
    }


    public void putBatch(String key, final Object... objects) throws IOException {
        new OutputStreamFactory(key) {

            @Override
            protected Void performTransaction(Output output) {
                for (Object object : objects) {
                    kryo.writeClassAndObject(output, object);
                }
                return null;
            }
        }.run();
    }

    public List<Object> getBatch(String key) throws IOException {
        return new InputStreamFactory<List<Object>>(key) {

            @Override
            protected List<Object> performTransaction(Input input) {
                List<Object> objects = new ArrayList<>();
                Object object;
                do {
                    object = kryo.readClassAndObject(input);
                    if(object != null)
                        objects.add(object);
                } while (object != null);
                return objects;
            }
        }.run();
    }

    public boolean has(String key) {
        if(dead)
            throw new DBDestroyedException();
        File file = getFile(key);
        return file.exists();
    }

    public boolean delete(String key) {
        if(dead)
            throw new DBDestroyedException();
        File file = getFile(key);
        return file.exists() && file.delete();
    }


    public String[] getKeys() {
        if(dead)
            throw new DBDestroyedException();
        List<File> files = getFiles(folder);
        String[] keys = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            keys[i] = getKey(file);
        }
        return keys;
    }

    public String[] getKeys(String prefix) {
        if(dead)
            throw new DBDestroyedException();
        String[] parts = prefix.split(SEPARATOR);
        File dir = folder;
        if(parts.length > 0)
            for (String part : parts)
                dir = new File(dir, part);
        List<File> files = getFiles(dir);
        String[] keys = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            keys[i] = getKey(file);
        }
        return keys;
    }

    public boolean destroy() {
        boolean success = true;
        List<File> files = getFiles(folder);
        for (File file : files)
            if(!file.delete())
                success = false;
        dead = success;
        return success;
    }

    private abstract class StreamFactory <STREAM, T> {
        protected String key;

        private StreamFactory(String key) {
            this.key = key;
        }

        T run() throws IOException {
            if(dead)
                throw new DBDestroyedException();
            STREAM item = initialize();
            T object = performTransaction(item);
            finish(item);
            return object;
        }

        protected abstract STREAM initialize() throws IOException;

        protected abstract T performTransaction(STREAM stream);

        protected abstract void finish(STREAM stream) throws IOException;
    }

    private abstract class InputStreamFactory <T> extends StreamFactory<Input, T> {
        private InputStream inputStream;

        private InputStreamFactory(String key) {
            super(key);
        }

        @Override
        protected Input initialize() throws IOException {
            inputStream = new FileInputStream(getFile(key));
            return new Input(inputStream);
        }

        @Override
        protected void finish(Input stream) throws IOException{
            if(stream != null)
                stream.close();
            if(inputStream != null)
                inputStream.close();
        }
    }
    private abstract class OutputStreamFactory extends StreamFactory<Output, Void> {
        private OutputStream outputStream;
        private OutputStreamFactory(String key) {
            super(key);
        }

        @Override
        protected Output initialize() throws IOException {
            outputStream = new FileOutputStream(getFile(key));
            return new Output(outputStream);
        }

        @Override
        protected void finish(Output stream) throws IOException {
            if(stream != null)
                stream.close();
            if(outputStream != null)
                outputStream.close();
        }
    }
}

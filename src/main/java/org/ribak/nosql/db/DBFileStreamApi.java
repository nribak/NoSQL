package org.ribak.nosql.db;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ribak.nosql.utils.exceptions.IllegalDirectoryException;
import org.ribak.nosql.utils.exceptions.InitializationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nribak on 10/03/2018.
 */

public class DBFileStreamApi implements IDBStreamApi {

    private static final String ROOT_DIR = "Kryo-DB";

    private static File ROOT_DIRECTORY;
    public static void init(Context context) {
        ROOT_DIRECTORY = new File(context.getFilesDir(), ROOT_DIR);
    }

    private File folder;

    DBFileStreamApi(String module) {
        if(ROOT_DIRECTORY == null)
            throw new InitializationException();
        if(ROOT_DIRECTORY.exists() || ROOT_DIRECTORY.mkdir())
            folder = new File(ROOT_DIRECTORY, module);
        if(folder == null || (!folder.exists() && !folder.mkdir()))
            throw new IllegalDirectoryException(folder);
    }

    @Override
    public InputStream getInputStream(String key) throws IOException {
        return new FileInputStream(Translator.getFile(folder, key));
    }

    @Override
    public OutputStream getOutputStream(String key) throws IOException {
        return new FileOutputStream(Translator.getFile(folder, key));
    }

    @Override
    public boolean keyExists(String key) {
        File file = Translator.getFile(folder, key);
        return file.exists();
    }

    @Override
    public boolean removeKey(String key) {
        File file = Translator.getFile(folder, key);
        return file.exists() && file.delete();
    }

    @Override
    public boolean deleteAll() {
        boolean success = true;
        File[] files = folder.listFiles();
        for (File file : files)
            success = success && file.delete();
        return success;
    }

    @Override
    public Set<String> getKeys(@NonNull String regex) {
        File[] allFiles = folder.listFiles();
        Set<String> keys = new HashSet<>();
        for (File file : allFiles)
            if(file.getName().matches(regex))
                keys.add(Translator.getKey(file));
        return keys;
    }

    private static class Translator {
        private static final String SUFFIX = ".db";
        private static File getFile(File folder, String key) {
            return new File(folder, key + SUFFIX);
        }

        private static String getKey(File file) {
            String name = file.getName();
            return name.substring(0, name.length() - SUFFIX.length());
        }
    }
}

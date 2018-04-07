package org.ribak.nosql.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.ribak.nosql.utils.KryoFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nribak on 06/03/2018.
 */

public class KryoDatabaseAPI extends DatabaseAPI {

    private Kryo kryo;
    private Input input;
    private Output output;

    KryoDatabaseAPI(String module) {
        super(module);
        kryo = KryoFactory.newInstance();
    }

    @NonNull
    @Override
    IDBStreamApi createStreamApi(String module) {
        return new DBFileStreamApi(module);
    }

    @Override
    void write(OutputStream outputStream, @Nullable Object value) {
        if(output == null)
            output = new Output(outputStream);
        else
            output.setOutputStream(outputStream);
        kryo.writeClassAndObject(output, value);
        output.flush();
    }

    @Nullable
    @Override
    Object read(InputStream inputStream) {
        if(input == null)
            input = new Input(inputStream);
        else
            input.setInputStream(inputStream);
        return kryo.readClassAndObject(input);
    }

    @Override
    public boolean supportsBatching() {
        return true;
    }
}

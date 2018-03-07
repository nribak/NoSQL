package org.ribak.nosql.serializers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by nribak on 27/12/2017.
 */

public class BitmapSerializer extends Serializer<Bitmap> {

    @Override
    public void write(Kryo kryo, Output output, Bitmap object) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        object.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.writeInt(byteArray.length);
        output.writeBytes(byteArray);
        output.writeString(object.getConfig().name());
    }

    @Override
    public Bitmap read(Kryo kryo, Input input, Class<Bitmap> type) {
        int length = input.readInt();
        byte[] byteArray = input.readBytes(length);
        String configName = input.readString();
        Bitmap.Config config = Bitmap.Config.valueOf(configName);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }
}

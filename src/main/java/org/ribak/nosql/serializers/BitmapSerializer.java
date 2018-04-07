package org.ribak.nosql.serializers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by nribak on 27/12/2017.
 */

public class BitmapSerializer extends Serializer<Bitmap> {

    @Override
    public void write(Kryo kryo, Output output, Bitmap object) {
        object.compress(Bitmap.CompressFormat.PNG, 100, output);
    }

    @Override
    public Bitmap read(Kryo kryo, Input input, Class<Bitmap> type) {
        return BitmapFactory.decodeStream(input);
    }
}

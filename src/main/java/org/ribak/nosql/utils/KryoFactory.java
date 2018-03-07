package org.ribak.nosql.utils;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import org.ribak.nosql.serializers.BitmapSerializer;
import org.ribak.nosql.serializers.BundleSerializer;

/**
 * Created by nribak on 26/02/2018.
 */

public class KryoFactory {

    public static Kryo newInstance() {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.addDefaultSerializer(Bitmap.class, new BitmapSerializer());
        kryo.addDefaultSerializer(Bundle.class, new BundleSerializer());
        return kryo;
    }
}

package org.ribak.nosql.serializers;

import android.os.Bundle;
import android.os.Parcelable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nribak on 07/03/2018.
 */

public class BundleSerializer extends Serializer<Bundle> {

    private Map<String, Object> map;

    public BundleSerializer() {
        map = new HashMap<>();
    }

    @Override
    public void write(Kryo kryo, Output output, Bundle object) {
        map.clear();
        for (String key : object.keySet())
            map.put(key, object.get(key));
        kryo.writeClassAndObject(output, map);
    }

    @Override
    public Bundle read(Kryo kryo, Input input, Class<Bundle> type) {
        Map<String, Object> map = (Map<String, Object>) kryo.readClassAndObject(input);
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof Parcelable)
                bundle.putParcelable(entry.getKey(), (Parcelable) entry.getValue());
            else if(entry.getValue() instanceof Serializable)
                bundle.putSerializable(entry.getKey(), (Serializable) entry.getValue());
        }
        new Bundle(bundle);
        return bundle;
    }
}

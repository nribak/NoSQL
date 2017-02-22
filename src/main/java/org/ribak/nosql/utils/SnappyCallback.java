package org.ribak.nosql.utils;

import android.support.annotation.Nullable;

/**
 * Created by nribak on 16/11/2016.
 */

public interface SnappyCallback <T>
{
    void onDbCallback(@Nullable String key, T response);
}

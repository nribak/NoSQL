package org.ribak.nosql.utils.exceptions;

import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by nribak on 21/02/2017.
 */

public class IllegalDirectoryException extends KryoDBException {

    private static final String MSG = "Cannot create directory ";
    public IllegalDirectoryException(@Nullable File directory) {
        super((directory == null) ? MSG : MSG + directory.getPath());
    }
}
